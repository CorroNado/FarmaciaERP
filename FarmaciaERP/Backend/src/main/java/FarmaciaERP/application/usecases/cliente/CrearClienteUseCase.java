package FarmaciaERP.application.usecases;

import FarmaciaERP.application.dto.Request.CrearClienteRequest;
import FarmaciaERP.application.dto.Response.CrearClienteResponse;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.repositories.IClienteRepository;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrearClienteUseCase {

    private final IClienteRepository clienteRepository;

    public CrearClienteUseCase(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public CrearClienteResponse ejecutar(CrearClienteRequest request) {
        Dni dni = new Dni(request.getDni());
        FullName fullName = new FullName(request.getNombre(), request.getApellido());
        Optional<Cliente> existente = clienteRepository.findByDni(dni);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con el documento: " + request.getDni());
        }
        clienteRepository.save(
                new Cliente(
                        fullName,
                        dni,
                        request.getInsuranceType()
                )
        );
        return new CrearClienteResponse(dni.getValor());
    }
}
