package FarmaciaERP.Domain.Enums;

public enum EstadoAsistencia {
    PROGRAMADO("Programado"),
    PENDIENTE("Pendiente"),
    A_TIEMPO("A tiempo"),
    TARDANZA("Tardanza"),
    FALTA_INJUSTIFICADA("Falta Injustificada"),
    JUSTIFICADO("Justificado");

    private final String descripcion;

    EstadoAsistencia(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
