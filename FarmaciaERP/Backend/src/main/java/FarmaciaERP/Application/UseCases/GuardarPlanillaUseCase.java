package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.BonoMetasEmpleadoRequest;
import FarmaciaERP.Application.DTOs.Request.GuardarPlanillaRequest;
import FarmaciaERP.Application.DTOs.Response.PlanillaResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.Planilla;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import FarmaciaERP.Domain.Repositories.IPlanillaRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * RRHH.03 Fase de Nómina - Guardar Planilla: espejo de guardarPlanilla()
 * del prototipo. Recalcula la planilla del mes/año (aplicando los bonos
 * por metas editados manualmente) y la persiste. RN-PLA-08: si ya existe
 * una planilla guardada para el mismo mes/año, exige confirmación
 * explícita de sobrescritura.
 */
@Service
public class GuardarPlanillaUseCase {

    private final IPlanillaRepository planillaRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IRegistroAsistenciaRepository registroAsistenciaRepository;

    public GuardarPlanillaUseCase(IPlanillaRepository planillaRepository, IEmpleadoRepository empleadoRepository,
                                   IRegistroAsistenciaRepository registroAsistenciaRepository) {
        this.planillaRepository = planillaRepository;
        this.empleadoRepository = empleadoRepository;
        this.registroAsistenciaRepository = registroAsistenciaRepository;
    }

    @Transactional
    public PlanillaResponse ejecutar(GuardarPlanillaRequest request) {
        var existente = planillaRepository.findByMesAndAnio(request.getMes(), request.getAnio());
        if (existente.isPresent() && !request.isConfirmarSobrescritura()) {
            throw new BadRequestException(
                    "RN-PLA-08: ya existe una planilla guardada para " + request.getMes() + "/" + request.getAnio()
                            + ". Confirme la sobrescritura para reemplazarla.");
        }

        List<Empleado> activos = empleadoRepository.findByEstado(EstadoEmpleado.ACTIVO);

        Map<Long, List<RegistroAsistencia>> registrosPorEmpleado = activos.stream()
                .collect(Collectors.toMap(Empleado::getId,
                        (Function<Empleado, List<RegistroAsistencia>>) empleado ->
                                registroAsistenciaRepository.findByEmpleadoIdAndMes(
                                        empleado.getId(), request.getMes(), request.getAnio())));

        Map<Long, Double> bonosMetas = (request.getBonosMetas() == null ? List.<BonoMetasEmpleadoRequest>of()
                : request.getBonosMetas()).stream()
                .collect(Collectors.toMap(BonoMetasEmpleadoRequest::getEmpleadoId, BonoMetasEmpleadoRequest::getBonoMetas));

        Planilla planilla = Planilla.calcular(request.getMes(), request.getAnio(), activos,
                registrosPorEmpleado, bonosMetas);

        planilla.marcarComoGuardada(existente.map(Planilla::getId).orElse(null));

        Planilla guardada = planillaRepository.save(planilla);
        return PlanillaResponseAssembler.toResponse(guardada);
    }
}
