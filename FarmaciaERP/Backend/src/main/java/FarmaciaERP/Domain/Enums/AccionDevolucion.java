package FarmaciaERP.Domain.Enums;

public enum AccionDevolucion {
    NOTA_CREDITO("Nota de crédito"),
    REEMBOLSO_EFECTIVO("Reembolso efectivo"),
    CAMBIO_PRODUCTO("Cambio de producto");

    private final String descripcion;

    AccionDevolucion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static AccionDevolucion fromString(String valor) {
        for (AccionDevolucion accion : AccionDevolucion.values()) {
            if (accion.name().equalsIgnoreCase(valor) || accion.descripcion.equalsIgnoreCase(valor)) {
                return accion;
            }
        }
        throw new IllegalArgumentException("Acción de devolución inválida: " + valor);
    }
}
