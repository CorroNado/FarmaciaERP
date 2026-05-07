package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public BuscarClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Optional<Cliente> porId(Long id) {
        return pacienteRepository.findById(id);
    }

    public List<Cliente> todos() {
        return pacienteRepository.findAll();
    }

    public Optional<Cliente> porDocumento(Dni documento) {
        return pacienteRepository.findByDni(documento);
    }

    public List<Cliente> porNombre(FullName nombre) {
        return pacienteRepository.findByName(nombre);
    }

    public List<Cliente> porTipoSeguro(TipoSeguro tipoSeguro) {
        return pacienteRepository.findByInsurance(tipoSeguro);
    }
}
