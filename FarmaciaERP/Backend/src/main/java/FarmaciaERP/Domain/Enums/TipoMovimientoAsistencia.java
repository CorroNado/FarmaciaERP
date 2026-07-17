package FarmaciaERP.Domain.Enums;

public enum TipoMovimientoAsistencia {
    PROGRAMACION("Programación de turno"),
    CHECKIN("Checkin"),
    CHECKOUT("Checkout"),
    JUSTIFICACION("Justificación"),
    EDICION("Edición"),
    ELIMINACION("Eliminación"),
    CAMBIO_AUTOMATICO("Cambio automático de estado");

    private final String descripcion;

    TipoMovimientoAsistencia(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
