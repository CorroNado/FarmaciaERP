package FarmaciaERP.Domain.Enums;

public enum RolEmpleado {
    QUIMICO_FARMACEUTICO("Químico Farmacéutico"),
    TECNICO_FARMACIA("Técnico en Farmacia"),
    CAJA_ATENCION_CLIENTE("Caja y Atención al Cliente"),
    ALMACEN("Almacén"),
    ADMINISTRACION("Administración");

    private final String descripcion;

    RolEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
