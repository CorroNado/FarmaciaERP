package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;

import java.util.List;
import java.util.Optional;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;

public interface IUsuarioRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(int id);

    List<Usuario> findAll();

    void deleteById(int id);

    boolean existsById(int id);

    List<Usuario> findByStatus(UsuarioEstados estado);

    List<Usuario> findByName(FullName nombre);

    Optional<Usuario> findByEmail(Email email);
}
