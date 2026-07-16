package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 04 - Estados del ciclo de Conciliación de Débitos y Ajustes
 * Técnicos (RN-AR4-01).
 */
public enum EstadoDebitoAR {
    PENDIENTE_JUSTIFICACION("Débito calculado — pendiente de evaluar si es justificado"),
    JUSTIFICADO("Débito justificado — Contabilidad registra el débito"),
    NO_JUSTIFICADO("Débito no justificado — pendiente de tramitar reclamo"),
    RECLAMO_TRAMITADO("Reclamo tramitado ante el Área Técnica — Contabilidad sienta la pérdida"),
    AJUSTADO("Ajuste técnico contable aplicado — débito conciliado");

    private final String descripcion;

    EstadoDebitoAR(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
