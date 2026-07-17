package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * RRHH.03 Nómina / Planilla: agrega, para un mes/año dado, la línea de
 * planilla ({@link DetallePlanillaEmpleado}) de cada colaborador activo,
 * calculada con su sueldo base (Contratación) y sus registros de
 * asistencia del mes (Control de Asistencia). Espejo de
 * calcularPlanilla/guardarPlanilla del prototipo (planilla.js).
 */
@Getter
@Setter
public class Planilla {

    /** RN-PLA-01: mes calendario, 1 = Enero ... 12 = Diciembre. */
    private Long id;
    private int mes;
    private int anio;
    private List<DetallePlanillaEmpleado> detalles;
    private LocalDateTime fechaCalculo;
    private LocalDateTime fechaGuardado;

    private Planilla() {
    }

    /**
     * RN-PLA-01: calcula la planilla del mes/año para todos los
     * colaboradores activos recibidos, usando sus registros de asistencia
     * del mes y el bono por metas indicado manualmente para cada uno (si
     * no se indica, se asume 0).
     */
    public static Planilla calcular(int mes, int anio, List<Empleado> empleadosActivos,
                                     Map<Long, List<RegistroAsistencia>> registrosPorEmpleado,
                                     Map<Long, Double> bonosMetasPorEmpleado) {
        validarMesYAnio(mes, anio);
        if (empleadosActivos == null || empleadosActivos.isEmpty()) {
            throw new BadRequestException(
                    "RN-PLA-01: no hay colaboradores activos para calcular la planilla de " + mes + "/" + anio);
        }

        Map<Long, List<RegistroAsistencia>> registros = registrosPorEmpleado == null ? Map.of() : registrosPorEmpleado;
        Map<Long, Double> bonos = bonosMetasPorEmpleado == null ? Map.of() : bonosMetasPorEmpleado;

        List<DetallePlanillaEmpleado> detalles = empleadosActivos.stream()
                .map(empleado -> DetallePlanillaEmpleado.calcular(
                        empleado,
                        registros.getOrDefault(empleado.getId(), List.of()),
                        bonos.getOrDefault(empleado.getId(), 0.0)))
                .toList();

        Planilla planilla = new Planilla();
        planilla.mes = mes;
        planilla.anio = anio;
        planilla.detalles = detalles;
        planilla.fechaCalculo = LocalDateTime.now();
        planilla.fechaGuardado = null;
        return planilla;
    }

    private static void validarMesYAnio(int mes, int anio) {
        if (mes < 1 || mes > 12) {
            throw new BadRequestException("RN-PLA-01: el mes debe estar entre 1 (Enero) y 12 (Diciembre)");
        }
        if (anio < 2000 || anio > 2100) {
            throw new BadRequestException("RN-PLA-01: el año de la planilla no es válido");
        }
    }

    /**
     * RN-PLA-08: marca la planilla como guardada (persistida), respetando
     * el id ya asignado si se está sobrescribiendo una planilla existente
     * para el mismo mes/año.
     */
    public void marcarComoGuardada(Long idExistente) {
        this.id = idExistente;
        this.fechaGuardado = LocalDateTime.now();
    }

    public boolean estaGuardada() {
        return id != null;
    }

    public double getMontoTotalNeto() {
        return detalles.stream().mapToDouble(DetallePlanillaEmpleado::getSueldoNeto).sum();
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static Planilla reconstruir(Long id, int mes, int anio, List<DetallePlanillaEmpleado> detalles,
                                        LocalDateTime fechaCalculo, LocalDateTime fechaGuardado) {
        Planilla planilla = new Planilla();
        planilla.id = id;
        planilla.mes = mes;
        planilla.anio = anio;
        planilla.detalles = detalles;
        planilla.fechaCalculo = fechaCalculo;
        planilla.fechaGuardado = fechaGuardado;
        return planilla;
    }
}
