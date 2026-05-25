package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.UbigeoType;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Department {
    private Long id;
    private Ubigeo ubigeo;
    private String nombre;

    public Department(String nombre, Ubigeo ubigeo) {
        this.nombre = nombre;
        this.ubigeo = validateUbigeoType(ubigeo);
    }
    public Department(Long id, String nombre, Ubigeo ubigeo) {
        this.id = id;
        this.nombre = nombre;
        this.ubigeo = validateUbigeoType(ubigeo);
    }

    private Ubigeo validateUbigeoType(Ubigeo ubigeo) {
        if (ubigeo.getTipo() != UbigeoType.DEPARTAMENTO)
            throw new IllegalArgumentException("Ubigeo debe ser de tipo departamento");
        return ubigeo;
    }
}
