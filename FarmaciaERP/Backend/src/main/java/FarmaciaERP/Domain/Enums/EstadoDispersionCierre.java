package FarmaciaERP.Domain.Enums;

public enum EstadoDispersionCierre {
    EN_COMPILACION("En compilación — pendiente de compilar la propuesta de pago (F110) recibida de la Fase 05"),
    PENDIENTE_VALIDACION("Propuesta compilada — pendiente de validar duplicados y bloqueos"),
    CON_OBSERVACIONES("Con observaciones — posible transferencia duplicada, requiere corrección"),
    VALIDADA("Propuesta validada — sin duplicados ni bloqueos"),
    ARCHIVO_GENERADO("Archivo bancario plano (IDoc) generado — pendiente de firma digital"),
    FIRMADA("Firmada digitalmente con token bancario — pendiente de transferencias"),
    TRANSFERIDA("Transferencias ejecutadas en banca empresa — pendiente de extracto bancario"),
    EXTRACTO_IMPORTADO("Extracto bancario digital importado (FF.5) — pendiente de conciliación"),
    CONCLUIDA("Cuentas puente conciliadas — obligación con proveedor extinguida (ciclo FI-AP finalizado)");

    private final String descripcion;

    EstadoDispersionCierre(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
