package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Entities.Receta;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.RecetaEstados;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Enums.UsuarioEstados;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(int id);

    List<Usuario> findAll();

    void deleteById(int id);

    boolean existsById(int id);

    List<Usuario> findByStatus(UsuarioEstados estado);


    List<Usuario> buscarPorNombre(String nombre);

    Optional<Usuario> buscarPorGmail(String email);
}
