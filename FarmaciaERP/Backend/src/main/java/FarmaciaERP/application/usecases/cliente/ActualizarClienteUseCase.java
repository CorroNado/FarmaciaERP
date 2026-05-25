package FarmaciaERP.application.usecases.cliente;
import FarmaciaERP.domain.entities.Customer;
import FarmaciaERP.domain.repositories.IClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public ActualizarClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Customer ejecutar(Long id, Customer customerActualizado) {
        if (!pacienteRepository.existById(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        // Aquí podrías agregar validaciones extra antes de guardar
        return pacienteRepository.save(customerActualizado);
    }
}
