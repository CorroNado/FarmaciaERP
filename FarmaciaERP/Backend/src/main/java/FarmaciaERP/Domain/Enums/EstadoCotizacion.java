package FarmaciaERP.Domain.Enums;

public enum EstadoCotizacion {
    PENDIENTE("Pendiente"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada"),
    VENCIDA("Vencida");

    private final String descripcion;

    EstadoCotizacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean puedeCambiarA(EstadoCotizacion nuevoEstado) {
        switch (this) {
            case PENDIENTE:
                return nuevoEstado == ACEPTADA || nuevoEstado == RECHAZADA || nuevoEstado == VENCIDA;
            case ACEPTADA:
            case RECHAZADA:
            case VENCIDA:
                return false;
            default:
                return false;
        }
    }

    public static EstadoCotizacion fromString(String valor) {
        for (EstadoCotizacion estado : EstadoCotizacion.values()) {
            if (estado.name().equalsIgnoreCase(valor) || estado.descripcion.equalsIgnoreCase(valor)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de cotización inválido: " + valor);
    }
}
