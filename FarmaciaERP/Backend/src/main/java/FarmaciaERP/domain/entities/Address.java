package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.AddressStatus;
import FarmaciaERP.domain.enums.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Address {
    private Long id;
    private Long dueñoId;
    private OwnerType tipoDueño;
    private String descripcion;
    private AddressLabel etiqueta;
    private AddressStatus estado;
    private Long districtId;
}
