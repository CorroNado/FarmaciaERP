package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.domain.valueObjects.FullName;

import java.util.List;
import java.util.Optional;

import FarmaciaERP.domain.valueObjects.Telephone;
import FarmaciaERP.domain.valueObjects.usuario.Username;

public interface IUsuarioRepository {

    void save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);

    List<User> findAll();

    Optional<User> findById(Long id);
    List<User> findByStatus(UserStatus estado);
    List<User> findByFullName(FullName nombreCompleto);
    List<User> findByProfile(Long perfilId);
    Optional<User> findByUsername(Username username);
    Optional<User> findByEmail(EmailAddress emailAddress);
    List<User> findByTelephone(Telephone telefono);
    List<User> findByDepartment(Long departamentoId);
    List<User> findByProvince(Long provinceId);
    List<User> findByDistrict(Long districtId);
}
