package FarmaciaERP.Domain.Repositories;

import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Enums.TipoSeguro;

import java.util.List;
import java.util.Optional;
public interface IPacienteRepository {

    Paciente guardar(Paciente paciente);

    Optional<Paciente> buscarPorId(Integer id);

    List<Paciente> listarTodos();

    void eliminarPorId(Integer id);

    boolean existePorId(Integer id);

    Optional<Paciente> buscarPorDocumentoIdentidad(String documentoIdentidad);

    List<Paciente> buscarPorNombre(String nombre);

    List<Paciente> buscarPorTipoSeguro(TipoSeguro tipoSeguro);
}