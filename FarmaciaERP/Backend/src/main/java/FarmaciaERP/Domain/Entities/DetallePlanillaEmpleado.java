package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

/**
 * RRHH.03 Nómina / Planilla - línea de detalle por colaborador: espejo de
 * cada fila de la tabla del prototipo (planilla.js → calcularPlanilla),
 * calculada a partir del sueldo base del colaborador (Contratación) y sus
 * registros de asistencia del mes (Control de Asistencia).
 */
@Getter
@Setter
public class DetallePlanillaEmpleado {

    /** RN-PLA-05: minutos de una jornada completa (30 días * 8h/día), base del descuento por tardanzas. */
    private static final int MINUTOS_JORNADA_MENSUAL = 480;
    private static final double PORCENTAJE_BONO = 0.05;
    private static final double PORCENTAJE_ESSALUD = 0.09;
    private static final double PORCENTAJE_AFP = 0.10;
    private static final double PORCENTAJE_OTROS_DESCUENTOS = 0.05;
    /** RN-PLA-04: tope de minutos de tardanza acumulada para mantener el bono de cumplimiento. */
    private static final int TOPE_MINUTOS_BONO_CUMPLIMIENTO = 30;

    private Long id;
    private Empleado empleado;
    private double sueldoBase;
    private double sueldoDiario;
    private int diasConTurno;
    private int faltas;
    private int minutosTardanza;
    private double horasExtras;
    private double montoExtra;
    private double descuentoFaltas;
    private double descuentoTardanzas;
    private double bonoAsistencia;
    private double bonoCumplimiento;
    private double bonoMetas;
    private double essalud;
    private double afp;
    private double otrosDescuentos;
    private double sueldoNeto;

    private DetallePlanillaEmpleado() {
    }

    /**
     * RN-PLA-02 a RN-PLA-06: calcula la línea de planilla de un colaborador
     * para el mes, a partir de sus registros de asistencia.
     */
    public static DetallePlanillaEmpleado calcular(Empleado empleado, List<RegistroAsistencia> registrosMes,
                                                     double bonoMetas) {
        if (empleado == null) {
            throw new BadRequestException("El colaborador es obligatorio para calcular su línea de planilla");
        }
        if (bonoMetas < 0) {
            throw new BadRequestException("RN-PLA-06: el bono por metas no puede ser negativo");
        }

        DetallePlanillaEmpleado detalle = new DetallePlanillaEmpleado();
        detalle.empleado = empleado;

        // RN-PLA-02: sueldo base, diario (30 días) y por hora (30 días * 8h).
        double sueldoBase = empleado.getSalario();
        double sueldoDiario = sueldoBase / 30.0;
        double sueldoHora = sueldoBase / 240.0;
        detalle.sueldoBase = sueldoBase;
        detalle.sueldoDiario = sueldoDiario;

        List<RegistroAsistencia> registros = registrosMes == null ? List.of() : registrosMes;
        detalle.diasConTurno = registros.size();
        detalle.faltas = (int) registros.stream()
                .filter(r -> r.getEstado() == EstadoAsistencia.FALTA_INJUSTIFICADA).count();

        // RN-PLA-03: minutos de tardanza acumulados, según el inicio del turno de cada registro.
        int minutosTardanza = registros.stream()
                .filter(r -> r.getEstado() == EstadoAsistencia.TARDANZA)
                .mapToInt(DetallePlanillaEmpleado::minutosTardanzaDe)
                .sum();
        detalle.minutosTardanza = minutosTardanza;

        // RN-PLA-04: horas extras y su monto, usando las horas/factor ya calculados en el marcaje de salida.
        double horasExtras = 0;
        double montoExtra = 0;
        for (RegistroAsistencia r : registros) {
            Double horasExtraRegistro = r.getHorasExtras();
            if (horasExtraRegistro != null && horasExtraRegistro > 0) {
                double factor = r.getFactorExtra() != null ? r.getFactorExtra() : 1.5;
                horasExtras += horasExtraRegistro;
                montoExtra += horasExtraRegistro * sueldoHora * factor;
            }
        }
        detalle.horasExtras = horasExtras;
        detalle.montoExtra = montoExtra;

        // RN-PLA-05: descuentos por faltas y tardanzas.
        double descuentoFaltas = detalle.faltas * sueldoDiario;
        double descuentoTardanzas = (minutosTardanza / (double) MINUTOS_JORNADA_MENSUAL) * sueldoDiario;
        detalle.descuentoFaltas = descuentoFaltas;
        detalle.descuentoTardanzas = descuentoTardanzas;

        // RN-PLA-06: bonos automáticos por asistencia perfecta y por cumplimiento.
        boolean asistenciaPerfecta = detalle.faltas == 0 && minutosTardanza == 0;
        boolean cumpleMetas = detalle.faltas == 0 && minutosTardanza < TOPE_MINUTOS_BONO_CUMPLIMIENTO;
        detalle.bonoAsistencia = asistenciaPerfecta ? sueldoBase * PORCENTAJE_BONO : 0;
        detalle.bonoCumplimiento = cumpleMetas ? sueldoBase * PORCENTAJE_BONO : 0;
        detalle.bonoMetas = bonoMetas;

        // RN-PLA-07: descuentos legales (EsSalud, AFP y otros descuentos).
        detalle.essalud = sueldoBase * PORCENTAJE_ESSALUD;
        detalle.afp = sueldoBase * PORCENTAJE_AFP;
        detalle.otrosDescuentos = sueldoBase * PORCENTAJE_OTROS_DESCUENTOS;

        detalle.sueldoNeto = detalle.calcularSueldoNeto();
        return detalle;
    }

    private double calcularSueldoNeto() {
        return sueldoBase
                - descuentoFaltas
                - descuentoTardanzas
                + bonoAsistencia
                + bonoCumplimiento
                + bonoMetas
                + montoExtra
                - essalud
                - afp
                - otrosDescuentos;
    }

    private static int minutosTardanzaDe(RegistroAsistencia registro) {
        LocalTime horaEntrada = registro.getHoraEntrada();
        if (horaEntrada == null) {
            return 0;
        }
        int minutosReales = horaEntrada.getHour() * 60 + horaEntrada.getMinute();
        int inicioTurno = registro.getTurno().getHoraInicioMinutos();
        return Math.max(0, minutosReales - inicioTurno);
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static DetallePlanillaEmpleado reconstruir(Long id, Empleado empleado, double sueldoBase,
                                                        double sueldoDiario, int diasConTurno, int faltas,
                                                        int minutosTardanza, double horasExtras, double montoExtra,
                                                        double descuentoFaltas, double descuentoTardanzas,
                                                        double bonoAsistencia, double bonoCumplimiento,
                                                        double bonoMetas, double essalud, double afp,
                                                        double otrosDescuentos, double sueldoNeto) {
        DetallePlanillaEmpleado detalle = new DetallePlanillaEmpleado();
        detalle.id = id;
        detalle.empleado = empleado;
        detalle.sueldoBase = sueldoBase;
        detalle.sueldoDiario = sueldoDiario;
        detalle.diasConTurno = diasConTurno;
        detalle.faltas = faltas;
        detalle.minutosTardanza = minutosTardanza;
        detalle.horasExtras = horasExtras;
        detalle.montoExtra = montoExtra;
        detalle.descuentoFaltas = descuentoFaltas;
        detalle.descuentoTardanzas = descuentoTardanzas;
        detalle.bonoAsistencia = bonoAsistencia;
        detalle.bonoCumplimiento = bonoCumplimiento;
        detalle.bonoMetas = bonoMetas;
        detalle.essalud = essalud;
        detalle.afp = afp;
        detalle.otrosDescuentos = otrosDescuentos;
        detalle.sueldoNeto = sueldoNeto;
        return detalle;
    }
}
