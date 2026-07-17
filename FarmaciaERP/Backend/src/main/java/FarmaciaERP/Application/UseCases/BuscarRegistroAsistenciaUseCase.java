package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuscarRegistroAsistenciaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;

    public BuscarRegistroAsistenciaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
    }

    public Optional<RegistroAsistenciaResponse> porId(Long id) {
        return registroAsistenciaRepository.findById(id).map(RegistroAsistenciaResponseAssembler::toResponse);
    }

    public List<RegistroAsistenciaResponse> todos() {
        return registroAsistenciaRepository.findAll().stream()
                .map(RegistroAsistenciaResponseAssembler::toResponse).toList();
    }

    public List<RegistroAsistenciaResponse> porFecha(LocalDate fecha) {
        return registroAsistenciaRepository.findByFecha(fecha).stream()
                .map(RegistroAsistenciaResponseAssembler::toResponse).toList();
    }

    public List<RegistroAsistenciaResponse> porEmpleado(Long empleadoId) {
        return registroAsistenciaRepository.findByEmpleadoId(empleadoId).stream()
                .map(RegistroAsistenciaResponseAssembler::toResponse).toList();
    }

    public List<RegistroAsistenciaResponse> porEmpleadoYMes(Long empleadoId, int mes, int anio) {
        return registroAsistenciaRepository.findByEmpleadoIdAndMes(empleadoId, mes, anio).stream()
                .map(RegistroAsistenciaResponseAssembler::toResponse).toList();
    }
}
