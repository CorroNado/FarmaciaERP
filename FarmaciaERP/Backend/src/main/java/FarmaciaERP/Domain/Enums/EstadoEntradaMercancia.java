package FarmaciaERP.Domain.Enums;

public enum EstadoEntradaMercancia {
    REGISTRADA("Registrada — en cuarentena"),
    SUSPENDIDA("Suspendida — diferencia excede tolerancia");

    private final String descripcion;

    EstadoEntradaMercancia(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
