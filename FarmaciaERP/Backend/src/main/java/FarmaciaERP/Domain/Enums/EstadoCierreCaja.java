package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 01 - Estados del ciclo de Recepción y Auditoría del Cierre
 * de Venta (RN-AR1-01), desde la emisión del reporte POS hasta la
 * clasificación automática de copagos y coberturas que habilita la Fase 02.
 */
public enum EstadoCierreCaja {
    ABIERTO("Cierre de caja emitido, arqueo pendiente"),
    CUADRADO("Arqueo físico cuadrado al 100%"),
    CON_VARIACION("Arqueo con variación, requiere justificación"),
    JUSTIFICADO("Descuadre justificado, físicos de recetas pendientes"),
    LIBERADO("Habilitado para clasificación automática"),
    CLASIFICADO("Copagos y coberturas clasificados, listo para Fase 02");

    private final String descripcion;

    EstadoCierreCaja(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
