package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserRole;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.valueObjects.FullName;

import java.util.List;
import java.util.Optional;

import FarmaciaERP.domain.valueObjects.usuario.Username;

public interface IUsuarioRepository {

    User save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);

    Optional<User> findById(Long id);
    List<User> findAll();

    List<User> findByStatus(UserStatus estado);
    List<User> findByName(FullName nombre);
    List<User> findByRol(UserRole rol);

    Optional<User> findByUsername(Username username);
    boolean existsByUsername(Username username);

    Optional<User> findEmailPrincipalById(Long usuarioId);
    Optional<User> findByEmail(EmailContact emailContact);

    Optional<User> findAddressPrincipalById(Long usuarioId);


}
