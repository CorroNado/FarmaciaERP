package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearClienteRequest;
import FarmaciaERP.Application.DTOs.Response.CrearClienteResponse;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
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
        Optional<Cliente> existente = clienteRepository.buscarPorDocumentoIdentidad(dni);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con el documento: " + request.getDni());
        }
        clienteRepository.guardar(
                new Cliente(
                        fullName,
                        dni,
                        request.getTipoSeguro()
                )
        );
        return new CrearClienteResponse(dni.getDni());
    }
}