package FarmaciaERP.domain.enums;

import FarmaciaERP.domain.exceptions.InvalidEnumValueException;

public enum InsuranceType {
        SIN_SEGURO("Sin seguro"),
        SIS("SIS"),
        ESSALUD("ESSALUD"),
        OTRO("Otro");
    private final String descripcion;
    InsuranceType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static InsuranceType fromString(String valor) {
        for (InsuranceType tipo : InsuranceType.values()) {
            if (tipo.name().equalsIgnoreCase(valor)
                    || tipo.descripcion.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new InvalidEnumValueException("InsuranceType inválido: " + valor);
    }
}

