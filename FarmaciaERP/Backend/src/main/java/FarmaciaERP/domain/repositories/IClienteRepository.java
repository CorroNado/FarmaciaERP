//package FarmaciaERP.domain.repositories;
//
//import FarmaciaERP.domain.entities.Customer;
//import FarmaciaERP.domain.enums.InsuranceType;
//import FarmaciaERP.domain.valueObjects.Dni;
//import FarmaciaERP.domain.valueObjects.FullName;
//
//import java.util.List;
//import java.util.Optional;
//public interface IClienteRepository {
//
//    Customer save(Customer customer);
//
//    Optional<Customer> findById(Long distritoId);
//
//    List<Customer> findAll();
//
//    void deleteById(Long distritoId);
//
//    boolean existById(Long distritoId);
//
//    Optional<Customer> findByDni(Dni documentoIdentidad);
//
//    List<Customer> findByName(FullName nombre);
//
//    List<Customer> findByInsurance(InsuranceType insuranceType);
//}