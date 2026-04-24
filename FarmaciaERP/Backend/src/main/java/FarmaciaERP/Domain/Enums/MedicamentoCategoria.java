package FarmaciaERP.Domain.Enums;


public enum MedicamentoCategoria {

    VENTA_LIBRE("De venta libre"),
    RECETA_SIMPLE("Bajo receta simple"),
    RECETA_RETENIDA("Bajo receta retenida"),
    CONTROLADO("Controlado");

    private final String descripcion;

    MedicamentoCategoria(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean requiereReceta(){
        return
                this == RECETA_SIMPLE ||
                this == RECETA_RETENIDA ||
                this == CONTROLADO;
    }

    public static MedicamentoCategoria fromString(String valor) {
        for (MedicamentoCategoria cat : MedicamentoCategoria.values()) {
            if (cat.name().equalsIgnoreCase(valor)
                    || cat.descripcion.equalsIgnoreCase(valor)) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Categoría inválida: " + valor);
    }
}