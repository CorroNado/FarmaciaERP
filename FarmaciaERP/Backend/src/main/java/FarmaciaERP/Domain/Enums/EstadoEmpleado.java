package FarmaciaERP.Domain.Enums;

public enum EstadoEmpleado {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String descripcion;

    EstadoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
