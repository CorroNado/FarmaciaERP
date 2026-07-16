package FarmaciaERP.Domain.Enums;

public enum EstadoConvenio {
    VIGENTE("Vigente"),
    VENCIDO("Vencido"),
    SUSPENDIDO("Suspendido");

    private final String descripcion;

    EstadoConvenio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
