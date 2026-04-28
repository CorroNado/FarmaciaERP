package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public EliminarClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void ejecutar(int id) {
        if (!pacienteRepository.existePorId(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        pacienteRepository.eliminarPorId(id);
    }
}
