package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.UbigeoType;
import FarmaciaERP.domain.valueObjects.Ubigeo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Province {
    private Long id;
    private String name;
    private Ubigeo ubigeo;
    private Long departmentId;

    public Province(String name, Ubigeo ubigeo, Long departmentId) {
        this.name = name;
        this.departmentId = departmentId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    public Province(Long id, String name, Ubigeo ubigeo, Long departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    private Ubigeo validateUbigeoType(Ubigeo ubigeo) {
        if (ubigeo.getTipo() != UbigeoType.PROVINCIA)
            throw new IllegalArgumentException("Ubigeo debe ser de tipo provincia");
        return ubigeo;
    }
}
