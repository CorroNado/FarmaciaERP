package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 06 - Estados del ciclo de Compensación Final y Análisis
 * de Margen Neto (RN-AR6-01), cierre del circuito FI-AR del período.
 */
public enum EstadoCompensacionAR {
    COMPENSADO("Compensación automática aplicada sobre la cuenta del cliente"),
    REPORTE_GENERADO("Reporte de Rendimiento Comercial y Margen generado"),
    SALDO_CONFIRMADO("Saldo limpio confirmado en cuentas corrientes"),
    CERRADO("Cierre de Ingresos por Convenio completado — ciclo FI-AR finalizado");

    private final String descripcion;

    EstadoCompensacionAR(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
