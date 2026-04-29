package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;

import java.util.List;
import java.util.Optional;
public interface IClienteRepository {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Integer id);

    List<Cliente> listarTodos();

    void eliminarPorId(Integer id);

    boolean existePorId(Integer id);

    Optional<Cliente> buscarPorDocumentoIdentidad(Dni documentoIdentidad);

    List<Cliente> buscarPorNombres(FullName nombre);

    List<Cliente> buscarPorTipoSeguro(TipoSeguro tipoSeguro);
}