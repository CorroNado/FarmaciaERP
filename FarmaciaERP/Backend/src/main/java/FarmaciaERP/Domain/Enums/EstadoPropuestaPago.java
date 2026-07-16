package FarmaciaERP.Domain.Enums;

public enum EstadoPropuestaPago {
    EN_PARAMETRIZACION("En parametrización — pendiente de introducir parámetros de pago (F110)"),
    PROPUESTA_EJECUTADA("Propuesta de pago ejecutada — pendiente de revisión de excepciones y bloqueos"),
    CON_EXCEPCIONES("Con excepciones — datos bancarios desactualizados, requiere ajustar parámetros y reejecutar"),
    APROBADA("Propuesta de pago final aprobada — pendiente de ejecución de pago"),
    PAGO_EJECUTADO("Pago ejecutado en SAP — pendiente de generar archivos bancarios"),
    CONCLUIDA("Archivos bancarios generados — apta para Fase 06 (Dispersión Bancaria y Conciliación)");

    private final String descripcion;

    EstadoPropuestaPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
