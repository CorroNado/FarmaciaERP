package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 02 — Contabilización y Declaración de Valores (SAP FI-AR).
 * RN-AR2-01: se concilian los lotes de tarjetas POS (solo cuando el arqueo
 * de la Fase 01 registró variación), se contabiliza el asiento automatizado
 * de ventas y cuadraturas, se audita la integridad de firmas y troqueles de
 * las recetas médicas contra la pre-liquidación de la aseguradora y,
 * finalmente, se consolida el lote y se despacha la valija física hacia
 * Oficina Central, habilitando la Fase 03.
 */
@Getter
@Setter
public class ContabilizacionAR {

    private Long id;
    private CierreCaja cierreCaja;
    private LocalDateTime fecha;
    private boolean conciliacionPOS;
    private boolean asientoProcesado;
    private boolean ajusteDescuadre;
    private boolean recetasAuditadas;
    private Boolean recetasCorrectas;
    private String motivoObservacion;
    private boolean subsanacion;
    private boolean consolidado;
    private EstadoContabilizacionAR estado;

    private ContabilizacionAR() {
    }

    /**
     * Inicia la Fase 02 a partir de un cierre de caja de la Fase 01 que ya
     * fue clasificado (copagos y coberturas) y habilitado para continuar.
     */
    public static ContabilizacionAR iniciar(CierreCaja cierreCaja) {
        if (cierreCaja == null) {
            throw new BadRequestException("La contabilización debe originarse de un cierre de caja de la Fase 01");
        }
        if (!cierreCaja.puedeContinuarFase02()) {
            throw new BadRequestException(
                    "RN-AR2-01: el cierre de caja aún no fue clasificado (copagos y coberturas) en la Fase 01");
        }

        ContabilizacionAR contabilizacion = new ContabilizacionAR();
        contabilizacion.cierreCaja = cierreCaja;
        contabilizacion.fecha = LocalDateTime.now();
        contabilizacion.estado = EstadoContabilizacionAR.INICIADA;
        return contabilizacion;
    }

    private boolean tieneVariacion() {
        return !Boolean.TRUE.equals(cierreCaja.getCuadra());
    }

    /**
     * 2.1.1 - Conciliación Primaria de Lotes de Tarjetas Físicas (POS).
     * Solo aplica cuando el arqueo de la Fase 01 registró variación
     * (2.1.2 - Registro de Declaración de Diferencias en Pantalla ERP).
     */
    public void conciliarLotesPOS() {
        if (!tieneVariacion()) {
            throw new BadRequestException("La conciliación de lotes POS solo aplica cuando el arqueo registró variación");
        }
        if (this.conciliacionPOS) {
            throw new BadRequestException("Los lotes de tarjetas POS ya fueron conciliados");
        }
        this.conciliacionPOS = true;
    }

    /**
     * 2.2.2 - Procesar Asiento Automatizado de Ventas y Cuadraturas.
     */
    public void procesarAsientoContable() {
        if (tieneVariacion() && !this.conciliacionPOS) {
            throw new BadRequestException("Debe conciliar los lotes de tarjetas POS antes de procesar el asiento contable");
        }
        if (this.asientoProcesado) {
            throw new BadRequestException("El asiento automatizado de ventas y cuadraturas ya fue procesado");
        }
        this.asientoProcesado = true;
        this.estado = EstadoContabilizacionAR.ASIENTO_PROCESADO;
    }

    /**
     * 2.2.3 - Revisión y Ajuste de Asientos Descuadrados. Solo aplica cuando
     * el arqueo de la Fase 01 registró variación.
     */
    public void revisarAjusteAsientos() {
        if (!this.asientoProcesado) {
            throw new BadRequestException("Debe procesarse el asiento contable antes de revisar los ajustes");
        }
        if (!tieneVariacion()) {
            throw new BadRequestException("La revisión de ajustes descuadrados solo aplica cuando el arqueo registró variación");
        }
        if (this.ajusteDescuadre) {
            throw new BadRequestException("Los asientos descuadrados ya fueron revisados y ajustados");
        }
        this.ajusteDescuadre = true;
    }

    /**
     * 2.3.1 - Auditoría de Integridad y Firmas de Recetas Médicas, cruzada
     * contra la pre-liquidación emitida por la aseguradora. RN-AR2-01: la
     * primera pasada siempre exige verificación exhaustiva; si se detecta
     * una observación (troquel ilegible, firma faltante, etc.) el lote
     * queda pendiente de subsanación antes de poder consolidarse.
     */
    public void auditarRecetas(boolean conforme, String motivoObservacion) {
        if (!this.asientoProcesado) {
            throw new BadRequestException("Debe procesarse el asiento contable antes de auditar las recetas");
        }
        if (tieneVariacion() && !this.ajusteDescuadre) {
            throw new BadRequestException("Debe revisarse y ajustarse el descuadre antes de auditar las recetas");
        }
        if (this.recetasAuditadas && this.recetasCorrectas != null && this.recetasCorrectas) {
            throw new BadRequestException("Las recetas ya fueron auditadas y se encuentran conformes");
        }
        if (!conforme && (motivoObservacion == null || motivoObservacion.isBlank())) {
            throw new BadRequestException("Debe indicar el motivo de la observación detectada en la auditoría de recetas");
        }

        this.recetasAuditadas = true;
        this.recetasCorrectas = conforme;
        this.motivoObservacion = conforme ? null : motivoObservacion;
        this.estado = conforme ? EstadoContabilizacionAR.RECETAS_CONFORMES : EstadoContabilizacionAR.RECETAS_EN_OBSERVACION;
    }

    /**
     * 2.3.3 - Subsanación de Recetas y Solicitud de Duplicados a Sucursal /
     * Médico tratante.
     */
    public void solicitarDuplicadoReceta() {
        if (!Boolean.FALSE.equals(this.recetasCorrectas)) {
            throw new BadRequestException("La subsanación solo aplica cuando la auditoría detectó una observación en las recetas");
        }
        if (this.subsanacion) {
            throw new BadRequestException("Ya se solicitó el duplicado de receta a la sucursal");
        }
        this.subsanacion = true;
    }

    /**
     * Reintento de la auditoría de integridad tras recibir el duplicado de
     * la receta observada.
     */
    public void reintentarAuditoria() {
        if (!Boolean.FALSE.equals(this.recetasCorrectas) || !this.subsanacion) {
            throw new BadRequestException("Debe solicitarse el duplicado de la receta antes de reintentar la auditoría");
        }
        this.recetasCorrectas = true;
        this.motivoObservacion = null;
        this.estado = EstadoContabilizacionAR.RECETAS_CONFORMES;
    }

    /**
     * 2.3.2 - Consolidación del Lote y Despacho de Valija Física hacia
     * Oficina Central. Habilita la Fase 03 — Auditoría Médica e
     * Impugnación de Recetas.
     */
    public void consolidarLoteDespacharValija() {
        if (!Boolean.TRUE.equals(this.recetasCorrectas)) {
            throw new BadRequestException("Las recetas deben estar conformes antes de consolidar el lote");
        }
        if (this.consolidado) {
            throw new BadRequestException("El lote ya fue consolidado y la valija despachada");
        }
        this.consolidado = true;
        this.estado = EstadoContabilizacionAR.CONSOLIDADA;
    }

    /**
     * RN-AR2-01: condición de salida de la Fase 02 hacia la Fase 03 —
     * Auditoría Médica e Impugnación de Recetas.
     */
    public boolean puedeContinuarFase03() {
        return this.estado == EstadoContabilizacionAR.CONSOLIDADA;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static ContabilizacionAR reconstruir(Long id, CierreCaja cierreCaja, LocalDateTime fecha,
                                                 boolean conciliacionPOS, boolean asientoProcesado,
                                                 boolean ajusteDescuadre, boolean recetasAuditadas,
                                                 Boolean recetasCorrectas, String motivoObservacion,
                                                 boolean subsanacion, boolean consolidado,
                                                 EstadoContabilizacionAR estado) {
        ContabilizacionAR contabilizacion = new ContabilizacionAR();
        contabilizacion.id = id;
        contabilizacion.cierreCaja = cierreCaja;
        contabilizacion.fecha = fecha;
        contabilizacion.conciliacionPOS = conciliacionPOS;
        contabilizacion.asientoProcesado = asientoProcesado;
        contabilizacion.ajusteDescuadre = ajusteDescuadre;
        contabilizacion.recetasAuditadas = recetasAuditadas;
        contabilizacion.recetasCorrectas = recetasCorrectas;
        contabilizacion.motivoObservacion = motivoObservacion;
        contabilizacion.subsanacion = subsanacion;
        contabilizacion.consolidado = consolidado;
        contabilizacion.estado = estado;
        return contabilizacion;
    }
}
