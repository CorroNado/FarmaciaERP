package FarmaciaERP.application.usecases;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.repositories.IClienteRepository;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
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

    public List<Cliente> porInsuranceType(InsuranceType InsuranceType) {
        return pacienteRepository.findByInsurance(InsuranceType);
    }
}
