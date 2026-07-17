package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CalcularPlanillaRequest;
import FarmaciaERP.Application.DTOs.Response.PlanillaResponse;
import FarmaciaERP.Domain.Entities.Empleado;
import FarmaciaERP.Domain.Entities.Planilla;
import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * RRHH.03 Fase de Nómina - Calcular Planilla: espejo de calcularPlanilla()
 * del prototipo. Es un cálculo en memoria (no persiste nada) sobre los
 * colaboradores activos y sus registros de asistencia del mes/año
 * indicados, para previsualizar la planilla antes de guardarla.
 */
@Service
public class CalcularPlanillaUseCase {

    private final IEmpleadoRepository empleadoRepository;
    private final IRegistroAsistenciaRepository registroAsistenciaRepository;

    public CalcularPlanillaUseCase(IEmpleadoRepository empleadoRepository,
                                    IRegistroAsistenciaRepository registroAsistenciaRepository) {
        this.empleadoRepository = empleadoRepository;
        this.registroAsistenciaRepository = registroAsistenciaRepository;
    }

    public PlanillaResponse ejecutar(CalcularPlanillaRequest request) {
        List<Empleado> activos = empleadoRepository.findByEstado(EstadoEmpleado.ACTIVO);

        Map<Long, List<RegistroAsistencia>> registrosPorEmpleado = activos.stream()
                .collect(Collectors.toMap(Empleado::getId,
                        (Function<Empleado, List<RegistroAsistencia>>) empleado ->
                                registroAsistenciaRepository.findByEmpleadoIdAndMes(
                                        empleado.getId(), request.getMes(), request.getAnio())));

        Planilla planilla = Planilla.calcular(request.getMes(), request.getAnio(), activos,
                registrosPorEmpleado, Map.of());

        return PlanillaResponseAssembler.toResponse(planilla);
    }
}
