package FarmaciaERP.Domain.Enums;

public enum ResultadoConciliacion {
    MATCH_OK("Match exitoso — lista para programación de pago"),
    BLOQUEADO_MRBR("Bloqueada — pendiente de revisión en MRBR");

    private final String descripcion;

    ResultadoConciliacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
