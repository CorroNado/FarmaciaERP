package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Repositories.IPacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarPacienteUseCase {

    private final IPacienteRepository pacienteRepository;

    public EliminarPacienteUseCase(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void ejecutar(int id) {
        if (!pacienteRepository.existePorId(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        pacienteRepository.eliminarPorId(id);
    }
}
