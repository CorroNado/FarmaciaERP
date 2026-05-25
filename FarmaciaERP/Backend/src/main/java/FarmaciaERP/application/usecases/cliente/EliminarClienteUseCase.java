package FarmaciaERP.application.usecases.cliente;
import FarmaciaERP.domain.entities.Customer;
import FarmaciaERP.domain.repositories.IClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarClienteUseCase {

    private final IClienteRepository clienteRepository;

    public EliminarClienteUseCase(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void ejecutar(Long id) {
        Customer customer = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException
                        ("Customer con el id:" + id + "no existe."));
        clienteRepository.deleteById(id);
    }
}
