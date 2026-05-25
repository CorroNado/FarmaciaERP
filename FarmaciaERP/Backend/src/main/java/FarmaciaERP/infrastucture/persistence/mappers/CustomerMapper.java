package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Customer;
import FarmaciaERP.infrastucture.persistence.entities.CustomerJPA;

public class CustomerMapper {
    public static CustomerJPA ToEntity(Customer customer) {
        CustomerJPA entity = new CustomerJPA();
        entity.setId(customer.getId());
        entity.setNombres(FullnameMapper.toEmbeddable(customer.getNombres()));
        entity.setDni(DniMapper.toEmbeddable(customer.getDni()));
        entity.setInsuranceType(customer.getInsuranceType());
        return entity;
    }

    public static Customer ToDomain(CustomerJPA entity) {
        return new Customer(
                entity.getId(),
                FullnameMapper.toDomain(entity.getNombres()),
                DniMapper.toDomain(entity.getDni()),
                entity.getInsuranceType()
        );
    }
}
