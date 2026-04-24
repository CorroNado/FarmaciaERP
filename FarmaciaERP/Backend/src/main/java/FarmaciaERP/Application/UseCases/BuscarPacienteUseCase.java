package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IPacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarPacienteUseCase {

    private final IPacienteRepository pacienteRepository;

    public BuscarPacienteUseCase(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Optional<Paciente> porId(int id) {
        return pacienteRepository.buscarPorId(id);
    }

    public List<Paciente> todos() {
        return pacienteRepository.listarTodos();
    }

    public Optional<Paciente> porDocumento(String documento) {
        return pacienteRepository.buscarPorDocumentoIdentidad(documento);
    }

    public List<Paciente> porNombre(String nombre) {
        return pacienteRepository.buscarPorNombre(nombre);
    }

    public List<Paciente> porTipoSeguro(TipoSeguro tipoSeguro) {
        return pacienteRepository.buscarPorTipoSeguro(tipoSeguro);
    }
}
