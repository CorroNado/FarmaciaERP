package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 02 - Estados del ciclo de Contabilización y Declaración de
 * Valores (RN-AR2-01), desde la conciliación de lotes POS hasta el
 * despacho de la valija física consolidada hacia Oficina Central.
 */
public enum EstadoContabilizacionAR {
    INICIADA("Contabilización iniciada, conciliación de lotes POS pendiente"),
    ASIENTO_PROCESADO("Asiento automatizado de ventas y cuadraturas contabilizado"),
    RECETAS_EN_OBSERVACION("Auditoría detectó recetas con observación, subsanación pendiente"),
    RECETAS_CONFORMES("Firmas, troqueles y vigencia de recetas conformes"),
    CONSOLIDADA("Lote consolidado y valija física despachada a Oficina Central");

    private final String descripcion;

    EstadoContabilizacionAR(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
