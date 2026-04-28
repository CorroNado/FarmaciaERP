package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public BuscarClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Optional<Cliente> porId(int id) {
        return pacienteRepository.buscarPorId(id);
    }

    public List<Cliente> todos() {
        return pacienteRepository.listarTodos();
    }

    public Optional<Cliente> porDocumento(String documento) {
        return pacienteRepository.buscarPorDocumentoIdentidad(documento);
    }

    public List<Cliente> porNombre(String nombre) {
        return pacienteRepository.buscarPorNombre(nombre);
    }

    public List<Cliente> porTipoSeguro(TipoSeguro tipoSeguro) {
        return pacienteRepository.buscarPorTipoSeguro(tipoSeguro);
    }
}
