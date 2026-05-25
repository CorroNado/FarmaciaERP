package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Customer {
    private Long id;
    private FullName nombres;
    private Dni dni;
    private InsuranceType insuranceType;

    public Customer() {}

    public Customer(FullName nombres, Dni dni, InsuranceType insuranceType) {
        this.nombres = nombres;
        this.dni = dni;
        this.insuranceType = insuranceType;
    }
}
