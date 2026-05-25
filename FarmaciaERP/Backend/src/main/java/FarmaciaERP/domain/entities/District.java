package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.UbigeoType;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class District {
    private Long id;
    private String nombre;
    private Ubigeo ubigeo;
    private Long provinceId;

    public District(String nombre, Ubigeo ubigeo, Long provinceId) {
        this.nombre = nombre;
        this.provinceId = provinceId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    public District(Long id, String nombre, Ubigeo ubigeo, Long provinceId) {
        this.id = id;
        this.nombre = nombre;
        this.provinceId = provinceId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    private Ubigeo validateUbigeoType(Ubigeo ubigeo) {
        if (ubigeo.getTipo() != UbigeoType.DISTRITO)
            throw new IllegalArgumentException("Ubigeo debe ser de tipo DISTRITO");
        return ubigeo;
    }
}
