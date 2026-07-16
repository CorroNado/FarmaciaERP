package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 03 - Estados del ciclo de Auditoría Médica e Impugnación de
 * Recetas (RN-AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION).
 */
public enum EstadoRecetaAR {
    PENDIENTE("Pendiente de validar troqueles, firmas y vigencia"),
    VALIDANDO("Troqueles y firmas válidos, pendiente de comparar con pre-liquidación"),
    RECHAZADA("Rechazada — troqueles, firmas o vigencia no conformes"),
    APROBADA("Coincide con pre-liquidación — aprobada para liquidación"),
    IMPUGNANDO("Inconsistencia detectada — impugnación enviada a la aseguradora"),
    LIBERADA("Impugnación aceptada — liberada para cobro"),
    DEBITO("Impugnación rechazada — débito confirmado");

    private final String descripcion;

    EstadoRecetaAR(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
