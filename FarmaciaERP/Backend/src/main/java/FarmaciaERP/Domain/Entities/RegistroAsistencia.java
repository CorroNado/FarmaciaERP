package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsistencia;
import FarmaciaERP.Domain.Enums.TurnoAsistencia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * RRHH.02 - Control de Asistencia: espejo del módulo de asistencia del
 * prototipo (asistencia.js). Un colaborador activo programa un turno para un
 * día, marca su entrada (con estado automático según puntualidad) y su
 * salida (con cálculo de horas trabajadas y horas extras), o bien la
 * inasistencia se justifica. Toda edición o eliminación posterior queda
 * respaldada por un motivo de auditoría (ver {@link AsistenciaAuditLog}).
 */
@Getter
@Setter
public class RegistroAsistencia {

    /** RN-AST-02: máximo de turnos que un colaborador puede tener el mismo día. */
    private static final int MAX_TURNOS_POR_DIA = 2;
    /** RN-AST-02: descanso mínimo (minutos) cuando al menos uno de los turnos es corto (4h). */
    private static final int DESCANSO_MINIMO_CORTO_MIN = 3 * 60;
    /** RN-AST-02: descanso mínimo (minutos) cuando ambos turnos son largos (8h). */
    private static final int DESCANSO_MINIMO_LARGO_MIN = 8 * 60;

    private Long id;
    private Empleado empleado;
    private LocalDate fecha;
    private TurnoAsistencia turno;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private LocalDateTime checkinTimestamp;
    private Double horasTrabajadas;
    private Double horasExtras;
    private Double factorExtra;
    private EstadoAsistencia estado;
    private boolean registrado;
    private boolean justificado;
    private String motivoJustificacion;
    private LocalDateTime fechaCreacion;

    private RegistroAsistencia() {
    }

    /**
     * RN-AST-01 a RN-AST-04: programa un nuevo turno para el colaborador en
     * la fecha indicada, validando cupo del turno, colaborador activo,
     * fecha no pasada y las reglas de solapamiento/descanso frente a los
     * demás turnos que el colaborador ya tenga ese mismo día.
     *
     * @param registrosDelDia todos los registros de asistencia (de cualquier
     *                        colaborador) ya existentes para esa misma fecha.
     */
    public static RegistroAsistencia programar(Empleado empleado, LocalDate fecha, TurnoAsistencia turno,
                                                 List<RegistroAsistencia> registrosDelDia) {
        if (empleado == null) {
            throw new BadRequestException("El colaborador es obligatorio");
        }
        if (!empleado.estaActivo()) {
            throw new BadRequestException(
                    "RN-AST-03: el colaborador " + empleado.getNombreCompleto()
                            + " no está activo. Verifique en Contratación.");
        }
        if (fecha == null) {
            throw new BadRequestException("La fecha de programación es obligatoria");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new BadRequestException("RN-AST-04: no se puede registrar asistencia en una fecha pasada");
        }
        if (turno == null) {
            throw new BadRequestException("El turno laboral es obligatorio");
        }

        // RN-AST-01: cupo máximo de colaboradores por turno y día.
        long ocupadosEnTurno = registrosDelDia.stream().filter(r -> r.turno == turno).count();
        if (ocupadosEnTurno >= turno.getCupoMaximo()) {
            throw new BadRequestException(
                    "RN-AST-01: el turno " + turno.getDescripcion() + " ya tiene el cupo completo ("
                            + turno.getCupoMaximo() + ") para el día " + fecha);
        }

        // RN-AST-02: máximo 2 turnos/día, sin turnos repetidos y con descanso mínimo.
        List<RegistroAsistencia> turnosDelEmpleado = registrosDelDia.stream()
                .filter(r -> r.empleado.getId().equals(empleado.getId())).toList();
        if (turnosDelEmpleado.size() >= MAX_TURNOS_POR_DIA) {
            throw new BadRequestException(
                    "RN-AST-02: el colaborador ya tiene " + MAX_TURNOS_POR_DIA + " turnos para el día " + fecha
                            + ". No puede tener un tercero.");
        }
        if (turnosDelEmpleado.stream().anyMatch(r -> r.turno == turno)) {
            throw new BadRequestException(
                    "RN-AST-02: el colaborador ya tiene un turno de " + turno.getDescripcion() + " para el día "
                            + fecha + ". No puede tener dos turnos iguales.");
        }
        if (turnosDelEmpleado.size() == 1) {
            validarDescansoEntreTurnos(turno, turnosDelEmpleado.get(0).turno);
        }

        RegistroAsistencia registro = new RegistroAsistencia();
        registro.empleado = empleado;
        registro.fecha = fecha;
        registro.turno = turno;
        registro.estado = EstadoAsistencia.PROGRAMADO;
        registro.registrado = false;
        registro.justificado = false;
        registro.fechaCreacion = LocalDateTime.now();
        return registro;
    }

    private static void validarDescansoEntreTurnos(TurnoAsistencia nuevo, TurnoAsistencia existente) {
        int nuevoInicio = nuevo.getHoraInicioMinutos();
        int nuevoFin = nuevoInicio + nuevo.getDuracionHoras() * 60;
        int existenteInicio = existente.getHoraInicioMinutos();
        int existenteFin = existenteInicio + existente.getDuracionHoras() * 60;

        int primerInicio, primerFin, segundoInicio;
        if (nuevoInicio < existenteInicio) {
            primerInicio = nuevoInicio;
            primerFin = nuevoFin;
            segundoInicio = existenteInicio;
        } else {
            primerInicio = existenteInicio;
            primerFin = existenteFin;
            segundoInicio = nuevoInicio;
        }

        if (segundoInicio < primerFin) {
            throw new BadRequestException(
                    "RN-AST-02: los turnos " + nuevo.getDescripcion() + " y " + existente.getDescripcion()
                            + " se solapan. No pueden asignarse el mismo día.");
        }

        int descansoMinutos = segundoInicio - primerFin;
        boolean esCorto = !nuevo.esTurnoLargo() || !existente.esTurnoLargo();
        int descansoMinimo = esCorto ? DESCANSO_MINIMO_CORTO_MIN : DESCANSO_MINIMO_LARGO_MIN;
        if (descansoMinutos < descansoMinimo) {
            throw new BadRequestException(
                    "RN-AST-02: no hay descanso suficiente entre " + nuevo.getDescripcion() + " y "
                            + existente.getDescripcion() + ". Se necesitan al menos "
                            + (descansoMinimo / 60) + " horas de descanso.");
        }
    }

    /**
     * RN-AST-05: marca la entrada del colaborador y calcula automáticamente
     * el estado según la puntualidad frente al horario del turno.
     */
    public void marcarEntrada(LocalDateTime ahora) {
        if (justificado) {
            throw new BadRequestException("El registro ya fue justificado, no admite marcación de entrada");
        }
        if (registrado) {
            throw new BadRequestException("La entrada ya fue registrada para este turno");
        }
        int minutosDelDia = ahora.getHour() * 60 + ahora.getMinute();
        this.estado = turno.calcularEstadoPorMarcacion(minutosDelDia);
        this.horaEntrada = ahora.toLocalTime();
        this.checkinTimestamp = ahora;
        this.registrado = true;
        this.horaSalida = null;
        this.horasTrabajadas = null;
        this.horasExtras = null;
        this.factorExtra = null;
    }

    /**
     * RN-AST-06: marca la salida, calculando horas trabajadas, horas extras
     * (por encima de la duración del turno) y su factor (1.5x diurno,
     * 2.0x nocturno para Noche / Guardia).
     */
    public void marcarSalida(LocalDateTime ahora) {
        if (!registrado) {
            throw new BadRequestException("El colaborador no ha marcado entrada. No se puede registrar salida.");
        }
        if (horaSalida != null) {
            throw new BadRequestException("La salida ya fue registrada.");
        }

        long minutosTrabajados = ChronoUnit.MINUTES.between(checkinTimestamp, ahora);
        double horas = minutosTrabajados / 60.0;
        this.horasTrabajadas = horas;
        this.horasExtras = Math.max(0, horas - turno.getDuracionHoras());
        this.factorExtra = turno.getFactorHoraExtra();
        this.horaSalida = ahora.toLocalTime();
    }

    /**
     * RN-AST-07: justifica una inasistencia. Sólo aplica a registros que
     * aún no marcaron entrada.
     */
    public void justificar(String motivo) {
        if (registrado) {
            throw new BadRequestException("RN-AST-07: el registro ya tiene entrada marcada, no admite justificación");
        }
        if (justificado) {
            throw new BadRequestException("RN-AST-07: el registro ya fue justificado");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException("RN-AST-07: el motivo de la justificación es obligatorio");
        }
        this.justificado = true;
        this.motivoJustificacion = motivo;
        this.estado = EstadoAsistencia.JUSTIFICADO;
    }

    /**
     * RN-AST-08: edición auditada de hora/estado. Sólo se permite sobre
     * registros marcados como "A tiempo" o "Tardanza" (espejo de la UI del
     * prototipo) y siempre exige un motivo de auditoría.
     */
    public void editar(LocalTime nuevaHoraEntrada, EstadoAsistencia nuevoEstado, String motivoAuditoria) {
        if (motivoAuditoria == null || motivoAuditoria.isBlank()) {
            throw new BadRequestException("RN-AST-08: el motivo de auditoría es obligatorio para editar el registro");
        }
        if (estado != EstadoAsistencia.A_TIEMPO && estado != EstadoAsistencia.TARDANZA) {
            throw new BadRequestException(
                    "RN-AST-08: sólo se pueden editar registros en estado 'A tiempo' o 'Tardanza'");
        }
        if (nuevaHoraEntrada != null) {
            this.horaEntrada = nuevaHoraEntrada;
        }
        if (nuevoEstado != null) {
            this.estado = nuevoEstado;
        }
    }

    /**
     * RN-AST-09: recalcula dinámicamente el estado de un turno aún no
     * marcado (Programado → Pendiente → Falta Injustificada), espejo de
     * calculateDynamicStatus del prototipo.
     */
    public void actualizarEstadoDinamico(LocalDateTime ahora) {
        if (registrado || justificado) {
            return;
        }
        LocalDate hoy = ahora.toLocalDate();
        if (fecha.isBefore(hoy)) {
            this.estado = EstadoAsistencia.FALTA_INJUSTIFICADA;
            return;
        }
        if (fecha.isAfter(hoy)) {
            this.estado = EstadoAsistencia.PROGRAMADO;
            return;
        }
        int minutosDelDia = ahora.getHour() * 60 + ahora.getMinute();
        if (minutosDelDia < turno.getHoraInicioMinutos()) {
            this.estado = EstadoAsistencia.PROGRAMADO;
        } else if (minutosDelDia <= turno.getFinTardanzaMinutos()) {
            this.estado = EstadoAsistencia.PENDIENTE;
        } else {
            this.estado = EstadoAsistencia.FALTA_INJUSTIFICADA;
        }
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static RegistroAsistencia reconstruir(Long id, Empleado empleado, LocalDate fecha, TurnoAsistencia turno,
                                                   LocalTime horaEntrada, LocalTime horaSalida,
                                                   LocalDateTime checkinTimestamp, Double horasTrabajadas,
                                                   Double horasExtras, Double factorExtra, EstadoAsistencia estado,
                                                   boolean registrado, boolean justificado, String motivoJustificacion,
                                                   LocalDateTime fechaCreacion) {
        RegistroAsistencia registro = new RegistroAsistencia();
        registro.id = id;
        registro.empleado = empleado;
        registro.fecha = fecha;
        registro.turno = turno;
        registro.horaEntrada = horaEntrada;
        registro.horaSalida = horaSalida;
        registro.checkinTimestamp = checkinTimestamp;
        registro.horasTrabajadas = horasTrabajadas;
        registro.horasExtras = horasExtras;
        registro.factorExtra = factorExtra;
        registro.estado = estado;
        registro.registrado = registrado;
        registro.justificado = justificado;
        registro.motivoJustificacion = motivoJustificacion;
        registro.fechaCreacion = fechaCreacion;
        return registro;
    }
}
