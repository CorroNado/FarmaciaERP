package FarmaciaERP.Domain.Enums;

public enum EstadoExcepcionFacturacion {
    BLOQUEADA("Bloqueada — pendiente de revisión por el Analista de Cuentas por Pagar"),
    REVISADA("Revisada — pendiente de clasificación de la discrepancia"),
    NOTIFICADA("Clasificada y notificada a Compras / Category Manager");

    private final String descripcion;

    EstadoExcepcionFacturacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
