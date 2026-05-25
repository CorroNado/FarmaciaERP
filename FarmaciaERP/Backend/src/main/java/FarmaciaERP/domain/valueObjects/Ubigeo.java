package FarmaciaERP.domain.valueObjects;

import FarmaciaERP.domain.enums.UbigeoType;
import lombok.*;

@Value
public class Ubigeo {
    String ubigeoReniec;
    String ubigeoInei;
    UbigeoType tipo;

    public Ubigeo(String ubigeoReniec, String ubigeoInei, UbigeoType tipo) {
        this.tipo = tipo;
        this.ubigeoReniec = validateUbigeo(ubigeoReniec, tipo);
        this.ubigeoInei = validateUbigeo(ubigeoInei, tipo);
    }

    private String validateUbigeo(String ubigeo, UbigeoType tipo) {
        if (ubigeo == null || ubigeo.isBlank())
            throw new IllegalArgumentException("Ubigeo no debe ser nulo");

        String pattern = switch (tipo) {
            case DEPARTAMENTO -> "^\\d{2}$";
            case PROVINCIA    -> "^\\d{4}$";
            case DISTRITO     -> "^\\d{6}$";
        };

        if (!ubigeo.matches(pattern))
            throw new IllegalArgumentException(
                    "Ubigeo inválido para " + tipo + ". Ejemplo: " + exampleFor(tipo)
            );

        return ubigeo;
    }

    private String exampleFor(UbigeoType tipo) {
        return switch (tipo) {
            case DEPARTAMENTO -> "15";
            case PROVINCIA    -> "1501";
            case DISTRITO     -> "150101";
        };
    }
}
