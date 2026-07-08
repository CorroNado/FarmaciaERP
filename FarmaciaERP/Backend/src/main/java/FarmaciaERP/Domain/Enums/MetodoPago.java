package FarmaciaERP.Domain.Enums;

public enum MetodoPago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    YAPE("Yape"),
    PLIN("Plin"),
    TRANSFERENCIA("Transferencia");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static MetodoPago fromString(String valor) {
        for (MetodoPago metodo : MetodoPago.values()) {
            if (metodo.name().equalsIgnoreCase(valor) || metodo.descripcion.equalsIgnoreCase(valor)) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Método de pago inválido: " + valor);
    }
}
