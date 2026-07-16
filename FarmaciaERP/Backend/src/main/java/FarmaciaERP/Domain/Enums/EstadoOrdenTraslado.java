package FarmaciaERP.Domain.Enums;

/**
 * LOG.07 - Ciclo de vida de la Orden de Traslado (STO) generada en la
 * Fase 06 (Gestión de stocks y distribución capilar).
 */
public enum EstadoOrdenTraslado {
    EN_TRANSITO("En tránsito — no disponible para venta"),
    RECIBIDO("Recepción confirmada en POS — disponible para venta");

    private final String descripcion;

    EstadoOrdenTraslado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
