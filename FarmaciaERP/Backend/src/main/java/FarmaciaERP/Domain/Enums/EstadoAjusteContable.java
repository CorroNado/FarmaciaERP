package FarmaciaERP.Domain.Enums;

public enum EstadoAjusteContable {
    PENDIENTE_NOTA_CREDITO("Pendiente — a la espera de confirmar la recepción de la Nota de Crédito"),
    GESTION_RECLAMO("En gestión de reclamo — Nota de Crédito no recibida, se reclama al laboratorio/droguería"),
    NOTA_CREDITO_REGISTRADA("Nota de Crédito registrada en SAP — pendiente de asiento de regularización"),
    REGULARIZADA("Regularizada — asiento contable ejecutado y partida desbloqueada, apta para Fase 04");

    private final String descripcion;

    EstadoAjusteContable(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
