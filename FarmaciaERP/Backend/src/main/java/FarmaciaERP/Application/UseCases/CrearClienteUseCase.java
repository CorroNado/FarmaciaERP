package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearPacienteRequest;
import FarmaciaERP.Application.DTOs.Response.CrearPacienteResponse;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrearClienteUseCase {

    private final IClienteRepository pacienteRepository;

    public CrearClienteUseCase(IClienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public CrearPacienteResponse ejecutar(CrearPacienteRequest request) {
        Optional<Cliente> existente = pacienteRepository.buscarPorDocumentoIdentidad(request.getDni());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con el documento: " + request.getDni());
        }

        return new CrearPacienteResponse(request.getDni());
    }
}