package FarmaciaERP.Domain.Enums;

public enum TipoDiscrepancia {
    PRECIO("Precio — desviación frente al vademécum / convenio pactado"),
    CANTIDAD("Cantidad — desviación frente a la cantidad recibida (MIGO)");

    private final String descripcion;

    TipoDiscrepancia(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
