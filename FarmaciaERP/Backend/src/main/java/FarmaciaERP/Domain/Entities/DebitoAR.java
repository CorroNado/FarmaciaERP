package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoDebitoAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 04 — Conciliación de Débitos y Ajustes Técnicos
 * (RN-AR4-01). Los débitos generados por recetas rechazadas o por
 * impugnaciones no aceptadas por la aseguradora (Fase 03) se evalúan:
 * si son justificados, Contabilidad los registra; si no lo son, se
 * tramita el reclamo ante el Área Técnica y se sienta la pérdida
 * correspondiente. En ambos casos el ciclo se cierra con la aplicación
 * del Ajuste Técnico Contable.
 */
@Getter
@Setter
public class DebitoAR {

    private Long id;
    private RecetaMedicaAR recetaMedicaAR;
    private double monto;
    private String motivo;
    private EstadoDebitoAR estado;
    private Boolean justificado;
    private boolean tramitado;
    private boolean ajustado;
    private LocalDateTime fecha;
    private LocalDateTime fechaAjuste;

    private DebitoAR() {
    }

    /**
     * RN-AR4-01: el débito se calcula automáticamente a partir de una
     * receta de la Fase 03 que quedó rechazada o con débito confirmado
     * por la aseguradora.
     */
    public static DebitoAR calcular(RecetaMedicaAR recetaMedicaAR) {
        if (recetaMedicaAR == null) {
            throw new BadRequestException("El débito debe originarse de una receta médica de la Fase 03");
        }
        if (!recetaMedicaAR.generaDebito()) {
            throw new BadRequestException(
                    "RN-AR4-01: la receta no generó débito — debe estar rechazada o con débito confirmado");
        }

        DebitoAR debito = new DebitoAR();
        debito.recetaMedicaAR = recetaMedicaAR;
        debito.monto = recetaMedicaAR.getMontoDeclarado();
        debito.motivo = recetaMedicaAR.getMotivoRechazo() != null
                ? recetaMedicaAR.getMotivoRechazo()
                : recetaMedicaAR.getInconsistencia();
        debito.estado = EstadoDebitoAR.PENDIENTE_JUSTIFICACION;
        debito.justificado = null;
        debito.tramitado = false;
        debito.ajustado = false;
        debito.fecha = LocalDateTime.now();
        return debito;
    }

    /**
     * RN-AR4-01: evaluación del débito — si es justificado, Contabilidad
     * registra el débito; si no, debe tramitarse el reclamo ante el Área
     * Técnica.
     */
    public void evaluarJustificacion(boolean justificado) {
        if (this.estado != EstadoDebitoAR.PENDIENTE_JUSTIFICACION) {
            throw new BadRequestException("El débito ya fue evaluado");
        }

        this.justificado = justificado;
        this.estado = justificado ? EstadoDebitoAR.JUSTIFICADO : EstadoDebitoAR.NO_JUSTIFICADO;
    }

    /**
     * RN-AR4-01: tramitación del reclamo ante el Área Técnica para los
     * débitos no justificados, previo a sentar la pérdida contable.
     */
    public void tramitarReclamo() {
        if (this.estado != EstadoDebitoAR.NO_JUSTIFICADO) {
            throw new BadRequestException("Solo se tramita reclamo sobre un débito no justificado");
        }
        if (this.tramitado) {
            throw new BadRequestException("El reclamo ya fue tramitado");
        }

        this.tramitado = true;
        this.estado = EstadoDebitoAR.RECLAMO_TRAMITADO;
    }

    /**
     * RN-AR4-01: aplica el Ajuste Técnico Contable que cierra el ciclo
     * del débito — ya sea por registrarse como débito justificado o por
     * sentarse la pérdida de un reclamo tramitado sin éxito.
     */
    public void aplicarAjusteTecnicoContable() {
        if (this.ajustado) {
            throw new BadRequestException("El ajuste técnico contable ya fue aplicado");
        }
        if (this.estado == EstadoDebitoAR.JUSTIFICADO) {
            // Contabilidad registra el débito justificado directamente.
        } else if (this.estado == EstadoDebitoAR.RECLAMO_TRAMITADO) {
            // Contabilidad sienta la pérdida por débito no subsanable.
        } else {
            throw new BadRequestException(
                    "RN-AR4-01: el débito debe estar justificado, o tramitado el reclamo, antes de aplicar el ajuste técnico contable");
        }

        this.ajustado = true;
        this.estado = EstadoDebitoAR.AJUSTADO;
        this.fechaAjuste = LocalDateTime.now();
    }

    /**
     * RN-AR4-01: el débito está conciliado cuando alcanzó el estado
     * terminal de esta fase (ajuste técnico contable aplicado).
     */
    public boolean estaConciliado() {
        return this.estado == EstadoDebitoAR.AJUSTADO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static DebitoAR reconstruir(Long id, RecetaMedicaAR recetaMedicaAR, double monto, String motivo,
                                        EstadoDebitoAR estado, Boolean justificado, boolean tramitado,
                                        boolean ajustado, LocalDateTime fecha, LocalDateTime fechaAjuste) {
        DebitoAR debito = new DebitoAR();
        debito.id = id;
        debito.recetaMedicaAR = recetaMedicaAR;
        debito.monto = monto;
        debito.motivo = motivo;
        debito.estado = estado;
        debito.justificado = justificado;
        debito.tramitado = tramitado;
        debito.ajustado = ajustado;
        debito.fecha = fecha;
        debito.fechaAjuste = fechaAjuste;
        return debito;
    }
}
