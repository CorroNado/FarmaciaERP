package FarmaciaERP.Domain.Enums;

public enum EstadoLotePago {
    EN_PREPARACION("En preparación — priorización, negociación y armado del lote (F110)"),
    PENDIENTE_APROBACION("Fondos verificados — pendiente de someter al Comité Semanal de Tesorería"),
    CON_OBSERVACIONES("Con observaciones del Comité — requiere corrección antes de volver a someterlo"),
    APROBADO("Aprobado por el Comité Semanal de Tesorería"),
    CONCILIADO_GESTION("Pagos ejecutados y conciliados a nivel de gestión — apto para Fase 05");

    private final String descripcion;

    EstadoLotePago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
