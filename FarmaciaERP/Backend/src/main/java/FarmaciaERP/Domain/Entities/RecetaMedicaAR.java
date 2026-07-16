package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoRecetaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 03 — Auditoría Médica e Impugnación de Recetas
 * (RN-AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION). Cada receta física
 * recibida en la valija consolidada de la Fase 02 se valida contra
 * requisitos médico-administrativos (troqueles, firmas, vigencia) y se
 * coteja con la pre-liquidación emitida por la aseguradora. Las
 * inconsistencias generan una impugnación formal ante la Obra Social /
 * Aseguradora, cuya respuesta libera la receta para cobro o confirma el
 * débito.
 */
@Getter
@Setter
public class RecetaMedicaAR {

    private Long id;
    private String numero;
    private ContabilizacionAR contabilizacionAR;
    private String medicamento;
    private String aseguradora;
    private double montoDeclarado;
    private double montoPreliquidado;
    private EstadoRecetaAR estado;
    private String motivoRechazo;
    private String inconsistencia;
    private LocalDateTime fecha;

    private RecetaMedicaAR() {
    }

    /**
     * Recepción física de la receta médica dentro del lote consolidado de
     * la Fase 02, en espera de validación de troqueles, firmas y vigencia.
     */
    public static RecetaMedicaAR registrar(String numero, ContabilizacionAR contabilizacionAR, String medicamento,
                                            String aseguradora, double montoDeclarado, double montoPreliquidado) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la receta es obligatorio");
        }
        if (contabilizacionAR == null) {
            throw new BadRequestException("La receta debe originarse del lote consolidado de la Fase 02");
        }
        if (!contabilizacionAR.isConsolidado()) {
            throw new BadRequestException("RN-AR3-01: el lote de la Fase 02 aún no fue consolidado y despachado");
        }
        if (medicamento == null || medicamento.isBlank()) {
            throw new BadRequestException("El medicamento de la receta es obligatorio");
        }
        if (aseguradora == null || aseguradora.isBlank()) {
            throw new BadRequestException("La aseguradora de la receta es obligatoria");
        }
        if (montoDeclarado < 0) {
            throw new BadRequestException("El monto declarado no puede ser negativo");
        }

        RecetaMedicaAR receta = new RecetaMedicaAR();
        receta.numero = numero;
        receta.contabilizacionAR = contabilizacionAR;
        receta.medicamento = medicamento;
        receta.aseguradora = aseguradora;
        receta.montoDeclarado = montoDeclarado;
        receta.montoPreliquidado = montoPreliquidado;
        receta.estado = EstadoRecetaAR.PENDIENTE;
        receta.fecha = LocalDateTime.now();
        return receta;
    }

    /**
     * Validación de troqueles, firmas y vigencia de la receta física.
     * RN-AR3-01: si no es válida, el rechazo se registra en SAP
     * (ZFMR_RECHAZO) y la receta queda fuera del circuito de cobro.
     */
    public void validarTroquelesFirmas(boolean valido, String motivoRechazo) {
        if (this.estado != EstadoRecetaAR.PENDIENTE) {
            throw new BadRequestException("La receta ya fue validada");
        }
        if (!valido && (motivoRechazo == null || motivoRechazo.isBlank())) {
            throw new BadRequestException("Debe indicar el motivo de rechazo de la receta");
        }

        if (valido) {
            this.estado = EstadoRecetaAR.VALIDANDO;
        } else {
            this.estado = EstadoRecetaAR.RECHAZADA;
            this.motivoRechazo = motivoRechazo;
        }
    }

    /**
     * Comparación contra la pre-liquidación emitida por la aseguradora.
     * RN-AR3-01: toda inconsistencia genera una impugnación formal
     * (ZFMR_IMPUGNACION) ante la Obra Social / Aseguradora.
     */
    public void compararPreliquidacion(boolean coincide, String inconsistencia) {
        if (this.estado != EstadoRecetaAR.VALIDANDO) {
            throw new BadRequestException(
                    "Debe validarse los troqueles, firmas y vigencia antes de comparar con la pre-liquidación");
        }
        if (!coincide && (inconsistencia == null || inconsistencia.isBlank())) {
            throw new BadRequestException("Debe indicar la inconsistencia detectada frente a la pre-liquidación");
        }

        if (coincide) {
            this.estado = EstadoRecetaAR.APROBADA;
        } else {
            this.estado = EstadoRecetaAR.IMPUGNANDO;
            this.inconsistencia = inconsistencia;
        }
    }

    /**
     * Registra la respuesta de la aseguradora a la impugnación enviada.
     */
    public void registrarRespuestaAseguradora(boolean aceptaImpugnacion) {
        if (this.estado != EstadoRecetaAR.IMPUGNANDO) {
            throw new BadRequestException("Solo se registra respuesta de la aseguradora sobre una receta en impugnación");
        }
        this.estado = aceptaImpugnacion ? EstadoRecetaAR.LIBERADA : EstadoRecetaAR.DEBITO;
    }

    /**
     * RN-AR3-01: una receta está procesada (fuera del circuito de
     * auditoría) cuando llegó a un estado terminal de esta fase.
     */
    public boolean estaProcesada() {
        return this.estado == EstadoRecetaAR.RECHAZADA
                || this.estado == EstadoRecetaAR.APROBADA
                || this.estado == EstadoRecetaAR.LIBERADA
                || this.estado == EstadoRecetaAR.DEBITO;
    }

    /**
     * RN-AR4-01: las recetas rechazadas o con débito confirmado generan
     * un débito técnico a conciliar en la Fase 04.
     */
    public boolean generaDebito() {
        return this.estado == EstadoRecetaAR.RECHAZADA || this.estado == EstadoRecetaAR.DEBITO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static RecetaMedicaAR reconstruir(Long id, String numero, ContabilizacionAR contabilizacionAR,
                                              String medicamento, String aseguradora, double montoDeclarado,
                                              double montoPreliquidado, EstadoRecetaAR estado, String motivoRechazo,
                                              String inconsistencia, LocalDateTime fecha) {
        RecetaMedicaAR receta = new RecetaMedicaAR();
        receta.id = id;
        receta.numero = numero;
        receta.contabilizacionAR = contabilizacionAR;
        receta.medicamento = medicamento;
        receta.aseguradora = aseguradora;
        receta.montoDeclarado = montoDeclarado;
        receta.montoPreliquidado = montoPreliquidado;
        receta.estado = estado;
        receta.motivoRechazo = motivoRechazo;
        receta.inconsistencia = inconsistencia;
        receta.fecha = fecha;
        return receta;
    }
}
