package FarmaciaERP.Domain.Enums;

/**
 * LOG.01 - Ciclo de vida de la Solicitud de Pedido (SolPed), desde su
 * creación respaldada por MRP (Fase 01) hasta su conversión en Orden de Compra (Fase 03).
 */
public enum EstadoSolPed {
    LIBERADA("Liberada"),
    FUENTE_APROBADA("Fuente de aprovisionamiento aprobada"),
    CONVERTIDA_OC("Convertida en Orden de Compra"),
    RECHAZADA("Rechazada");

    private final String descripcion;

    EstadoSolPed(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean puedeCambiarA(EstadoSolPed nuevoEstado) {
        return switch (this) {
            case LIBERADA -> nuevoEstado == FUENTE_APROBADA || nuevoEstado == RECHAZADA;
            case FUENTE_APROBADA -> nuevoEstado == CONVERTIDA_OC || nuevoEstado == RECHAZADA;
            case CONVERTIDA_OC, RECHAZADA -> false;
        };
    }
}
