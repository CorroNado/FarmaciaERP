package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;

import java.util.List;
import java.util.Optional;
public interface IClienteRepository {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Integer id);

    List<Cliente> listarTodos();

    void eliminarPorId(Integer id);

    boolean existePorId(Integer id);

    Optional<Cliente> buscarPorDocumentoIdentidad(String documentoIdentidad);

    List<Cliente> buscarPorNombre(String nombre);

    List<Cliente> buscarPorTipoSeguro(TipoSeguro tipoSeguro);
}