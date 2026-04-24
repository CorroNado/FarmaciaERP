package FarmaciaERP.Domain.Enums;

public enum RecetaEstados {
    VIGENTE("Vigente"),
    PENDIENTE_PAGO("Pendiende de pago"),
    PAGADO("Pagado"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado"),
    DISPENSADO("Dispensado");
    private final String descripcion;

    RecetaEstados(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public boolean CambiarEstadoA(RecetaEstados nuevoEstado) {
        switch (this) {
            case VIGENTE:
                return nuevoEstado == PENDIENTE_PAGO
                        || nuevoEstado == VENCIDO
                        || nuevoEstado == CANCELADO;

            case PENDIENTE_PAGO:
            case PAGADO:
                return nuevoEstado == DISPENSADO
                        || nuevoEstado == CANCELADO;

            case VENCIDO:
            case DISPENSADO:
            case CANCELADO:
                return false;

            default:
                return false;
        }
    }
}
