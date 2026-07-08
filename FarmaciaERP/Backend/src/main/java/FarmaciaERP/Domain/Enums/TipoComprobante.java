package FarmaciaERP.Domain.Enums;

public enum TipoComprobante {
    BOLETA("Boleta de venta"),
    FACTURA("Factura"),
    NINGUNO("Sin comprobante");

    private final String descripcion;

    TipoComprobante(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoComprobante fromString(String valor) {
        for (TipoComprobante tipo : TipoComprobante.values()) {
            if (tipo.name().equalsIgnoreCase(valor) || tipo.descripcion.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de comprobante inválido: " + valor);
    }
}
