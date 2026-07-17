package FarmaciaERP.Domain.Enums;

public enum TipoMovimientoRRHH {
    ALTA("Alta"),
    EDICION("Edición"),
    CAMBIO_ESTADO("Cambio Estado"),
    BAJA_INMEDIATA("Baja inmediata"),
    BAJA_PROGRAMADA("Baja programada"),
    CAMBIO_AUTOMATICO("Cambio automático de estado"),
    ELIMINACION("Eliminación");

    private final String descripcion;

    TipoMovimientoRRHH(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
