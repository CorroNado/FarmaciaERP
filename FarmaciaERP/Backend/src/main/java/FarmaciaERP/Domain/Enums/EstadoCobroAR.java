package FarmaciaERP.Domain.Enums;

/**
 * FI-AR · Fase 05 - Estados del ciclo de Procesamiento de Cobros e
 * Imputación Bancaria (RN-AR5-01), desde la interpretación del archivo
 * de transferencia hasta el registro del ingreso e imputación en la
 * cuenta del cliente.
 */
public enum EstadoCobroAR {
    INTERPRETADO("Archivo de transferencia bancaria interpretado"),
    CONCILIADO("Comisiones de tarjeta y retenciones conciliadas y cuadradas"),
    DESCUADRADO("Diferencia detectada en comisiones y retenciones — ajuste contable pendiente"),
    AJUSTADO("Ajuste contable por diferencia ingresado — montos cuadrados"),
    REGISTRADO("Ingreso de dinero registrado e imputado en la cuenta del cliente");

    private final String descripcion;

    EstadoCobroAR(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
