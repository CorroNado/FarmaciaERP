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
    private String nombre;
    private Ubigeo ubigeo;
    private Long departmentId;

    public Province(String nombre, Ubigeo ubigeo, Long departmentId) {
        this.nombre = nombre;
        this.departmentId = departmentId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    public Province(Long id, String nombre, Ubigeo ubigeo, Long departmentId) {
        this.id = id;
        this.nombre = nombre;
        this.departmentId = departmentId;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    private Ubigeo validateUbigeoType(Ubigeo ubigeo) {
        if (ubigeo.getTipo() != UbigeoType.PROVINCIA)
            throw new IllegalArgumentException("Ubigeo debe ser de tipo provincia");
        return ubigeo;
    }
}
