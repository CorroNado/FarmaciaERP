package FarmaciaERP.Domain.Enums;

public enum TipoContrato {
    INDEFINIDO("Indefinido"),
    TEMPORAL("Temporal"),
    PRACTICAS("Prácticas"),
    MEDIO_TIEMPO("Medio Tiempo");

    private final String descripcion;

    TipoContrato(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
