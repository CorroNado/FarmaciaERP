package FarmaciaERP.Domain.Enums;

public enum EstadoFacturaMIRO {
    REGISTRADA("Registrada"),
    ANULADA("Anulada");

    private final String descripcion;

    EstadoFacturaMIRO(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
