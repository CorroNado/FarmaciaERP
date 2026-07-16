package FarmaciaERP.Domain.Enums;

public enum EstadoPago {
    EJECUTADO("Ejecutado — cuenta por pagar cerrada (FIPO)"),
    ANULADO("Anulado");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
