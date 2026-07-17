package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ProgramarAsistenciaRequest;
import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.02 - Control de Asistencia: programa un nuevo turno ("Registrar
 * Entrada" del prototipo, que en realidad crea el registro en estado
 * "Programado" hasta que se marque la entrada real).
 */
@Service
public class ProgramarAsistenciaUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;
    private final IEmpleadoRepository empleadoRepository;

    public ProgramarAsistenciaUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository,
                                       IEmpleadoRepository empleadoRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public RegistroAsistenciaResponse ejecutar(ProgramarAsistenciaRequest request) {
        if (request.getEmpleadoId() == null) {
            throw new BadRequestException("El identificador del colaborador es obligatorio");
        }

        Empleado empleado = empleadoRepository.findById(request.getEmpleadoId())
                .orElseThrow(() -> new BadRequestException("Colaborador no encontrado: " + request.getEmpleadoId()));

        var registrosDelDia = registroAsistenciaRepository.findByFecha(request.getFecha());
        RegistroAsistencia registro = RegistroAsistencia.programar(
                empleado, request.getFecha(), request.getTurno(), registrosDelDia);

        RegistroAsistencia guardado = registroAsistenciaRepository.save(registro);
        return RegistroAsistenciaResponseAssembler.toResponse(guardado);
    }
}
