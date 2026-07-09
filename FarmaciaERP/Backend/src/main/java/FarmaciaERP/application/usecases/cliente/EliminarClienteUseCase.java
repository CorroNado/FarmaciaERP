package FarmaciaERP.application.usecases;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.repositories.IClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarClienteUseCase {

    private final IClienteRepository clienteRepository;

    public EliminarClienteUseCase(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void ejecutar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException
                        ("Cliente con el id:" + id + "no existe."));
        clienteRepository.deleteById(id);
    }
}
