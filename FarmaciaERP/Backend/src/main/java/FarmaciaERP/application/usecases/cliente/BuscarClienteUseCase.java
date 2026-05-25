package FarmaciaERP.application.usecases.cliente;
import FarmaciaERP.domain.entities.Customer;
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

    public Optional<Customer> porId(Long id) {
        return pacienteRepository.findById(id);
    }

    public List<Customer> todos() {
        return pacienteRepository.findAll();
    }

    public Optional<Customer> porDocumento(Dni documento) {
        return pacienteRepository.findByDni(documento);
    }

    public List<Customer> porNombre(FullName nombre) {
        return pacienteRepository.findByName(nombre);
    }

    public List<Customer> porTipoSeguro(InsuranceType insuranceType) {
        return pacienteRepository.findByInsurance(insuranceType);
    }
}
