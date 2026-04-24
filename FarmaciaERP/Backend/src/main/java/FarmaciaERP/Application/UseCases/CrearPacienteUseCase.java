package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Repositories.IPacienteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrearPacienteUseCase {

    private final IPacienteRepository pacienteRepository;

    public CrearPacienteUseCase(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente ejecutar(Paciente paciente) {
        Optional<Paciente> existente = pacienteRepository.buscarPorDocumentoIdentidad(paciente.getDni());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con el documento: " + paciente.getDni());
        }
        return pacienteRepository.guardar(paciente);
    }
}