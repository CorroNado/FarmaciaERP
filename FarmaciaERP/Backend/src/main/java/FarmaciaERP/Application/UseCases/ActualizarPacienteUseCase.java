package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Repositories.IPacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarPacienteUseCase {

    private final IPacienteRepository pacienteRepository;

    public ActualizarPacienteUseCase(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente ejecutar(int id, Paciente pacienteActualizado) {
        if (!pacienteRepository.existePorId(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        // Aquí podrías agregar validaciones extra antes de guardar
        return pacienteRepository.guardar(pacienteActualizado);
    }
}
