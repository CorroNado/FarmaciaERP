//package FarmaciaERP.infrastucture.persistence.mappers;
//
//import FarmaciaERP.domain.entities.Customer;
//import FarmaciaERP.infrastucture.persistence.entities.CustomerJPA;
//
//public class CustomerMapper {
//    public static CustomerJPA ToEntity(Customer customer) {
//        CustomerJPA entity = new CustomerJPA();
//        entity.setDistritoId(customer.getDistritoId());
//        entity.setNombres(FullnameMapper.toEmbeddable(customer.getNombres()));
//        entity.setDni(DniMapper.toEmbeddable(customer.getDni()));
//        entity.setInsuranceType(customer.getInsuranceType());
//        return entity;
//    }
//
//    public static Customer ToDomain(CustomerJPA entity) {
//        return new Customer(
//                entity.getDistritoId(),
//                FullnameMapper.toDomain(entity.getNombres()),
//                DniMapper.toDomain(entity.getDni()),
//                entity.getInsuranceType()
//        );
//    }
//}
