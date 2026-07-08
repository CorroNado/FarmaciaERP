package FarmaciaERP.Domain.Enums;

public enum EstadoVenta {
    PENDIENTE("Pendiente de pago"),
    PAGADA("Pagada"),
    ANULADA("Anulada"),
    DEVUELTA("Devuelta");

    private final String descripcion;

    EstadoVenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean puedeCambiarA(EstadoVenta nuevoEstado) {
        switch (this) {
            case PENDIENTE:
                return nuevoEstado == PAGADA || nuevoEstado == ANULADA;
            case PAGADA:
                return nuevoEstado == ANULADA || nuevoEstado == DEVUELTA;
            case ANULADA:
            case DEVUELTA:
                return false;
            default:
                return false;
        }
    }

    public static EstadoVenta fromString(String valor) {
        for (EstadoVenta estado : EstadoVenta.values()) {
            if (estado.name().equalsIgnoreCase(valor) || estado.descripcion.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de venta inválido: " + valor);
    }
}
