package FarmaciaERP.Domain.Enums;

public enum EstadoOrdenCompra {
    BORRADOR("Borrador"),
    FIRMADA("Firmada y despachada"),
    ANULADA("Anulada");

    private final String descripcion;

    EstadoOrdenCompra(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
