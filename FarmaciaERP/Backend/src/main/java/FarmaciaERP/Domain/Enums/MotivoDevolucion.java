package FarmaciaERP.Domain.Enums;

public enum MotivoDevolucion {
    PRODUCTO_DEFECTUOSO("Producto defectuoso"),
    ERROR_DESPACHO("Error de despacho"),
    CLIENTE_INSATISFECHO("Cliente insatisfecho"),
    PRODUCTO_PROXIMO_VENCER("Producto próximo a vencer"),
    OTRO("Otro");

    private final String descripcion;

    MotivoDevolucion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static MotivoDevolucion fromString(String valor) {
        for (MotivoDevolucion motivo : MotivoDevolucion.values()) {
            if (motivo.name().equalsIgnoreCase(valor) || motivo.descripcion.equalsIgnoreCase(valor)) {
                return motivo;
            }
        }
        throw new IllegalArgumentException("Motivo de devolución inválido: " + valor);
    }
}
