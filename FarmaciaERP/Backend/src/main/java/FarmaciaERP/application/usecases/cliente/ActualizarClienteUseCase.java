package FarmaciaERP.application.usecases;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.repositories.IClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public ActualizarClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Cliente ejecutar(Long id, Cliente clienteActualizado) {
        if (!pacienteRepository.existById(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        // Aqu├¡ podr├¡as agregar validaciones extra antes de guardar
        return pacienteRepository.save(clienteActualizado);
    }
}
