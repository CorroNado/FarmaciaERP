package FarmaciaERP.Domain.Enums;

import FarmaciaERP.Domain.Exceptions.InvalidEnumValueException;

public enum TipoSeguro {
        SIN_SEGURO("Sin seguro"),
        SIS("SIS"),
        ESSALUD("ESSALUD"),
        OTRO("Otro");
    private final String descripcion;
    TipoSeguro(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoSeguro fromString(String valor) {
        for (TipoSeguro tipo : TipoSeguro.values()) {
            if (tipo.name().equalsIgnoreCase(valor)
                    || tipo.descripcion.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new InvalidEnumValueException("TipoSeguro inválido: " + valor);
    }
}

