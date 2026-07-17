package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Enums.RolEmpleado;
import FarmaciaERP.Domain.Enums.TipoContrato;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * RRHH.01 - Contratación: maestro de colaboradores de la farmacia.
 * Replica la lógica de "bajas inteligentes" del prototipo: un colaborador con
 * turnos activos no se inactiva de inmediato, sino que puede darse de baja
 * inmediata (cancelando el turno) o programarse para el término de su último
 * turno vigente.
 */
@Getter
@Setter
public class Empleado {

    private Long id;
    private String codigo; // EMP-001, EMP-002...
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombres;
    private String dni;
    private RolEmpleado rol;
    private String area;
    private LocalDate fechaIngreso;
    private double salario;
    private TipoContrato contrato;
    private String correo;
    private String telefono;
    private EstadoEmpleado estado;

    // Baja programada (para el término del último turno activo del colaborador)
    private LocalDateTime bajaProgramadaFechaEfectiva;
    private String bajaProgramadaObservacion;
    private String bajaProgramadaTurnoInfo;

    public Empleado() {
    }

    public Empleado(String codigo, String apellidoPaterno, String apellidoMaterno, String nombres,
                     String dni, RolEmpleado rol, String area, LocalDate fechaIngreso, double salario,
                     TipoContrato contrato, String correo, String telefono) {
        validarDatosBasicos(apellidoPaterno, apellidoMaterno, nombres, dni, rol, salario);
        this.codigo = codigo;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombres = nombres;
        this.dni = dni;
        this.rol = rol;
        this.area = area;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.contrato = contrato;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = EstadoEmpleado.ACTIVO;
        this.bajaProgramadaFechaEfectiva = null;
        this.bajaProgramadaObservacion = null;
        this.bajaProgramadaTurnoInfo = null;
    }

    private void validarDatosBasicos(String apellidoPaterno, String apellidoMaterno, String nombres,
                                      String dni, RolEmpleado rol, double salario) {
        if (apellidoPaterno == null || apellidoPaterno.isBlank()) {
            throw new BadRequestException("El apellido paterno es obligatorio");
        }
        if (apellidoMaterno == null || apellidoMaterno.isBlank()) {
            throw new BadRequestException("El apellido materno es obligatorio");
        }
        if (nombres == null || nombres.isBlank()) {
            throw new BadRequestException("Los nombres son obligatorios");
        }
        if (dni == null || dni.isBlank()) {
            throw new BadRequestException("El DNI es obligatorio");
        }
        if (rol == null) {
            throw new BadRequestException("El rol es obligatorio");
        }
        if (salario < 0) {
            throw new BadRequestException("El salario debe ser un número positivo");
        }
    }

    public String getNombreCompleto() {
        return apellidoPaterno + " " + apellidoMaterno + " " + nombres;
    }

    public boolean estaActivo() {
        return estado == EstadoEmpleado.ACTIVO;
    }

    public boolean tieneBajaProgramada() {
        return bajaProgramadaFechaEfectiva != null;
    }

    /**
     * Actualiza los datos editables del colaborador (no toca el estado ni la baja programada).
     */
    public void actualizarDatos(String apellidoPaterno, String apellidoMaterno, String nombres, String dni,
                                 RolEmpleado rol, String area, LocalDate fechaIngreso, double salario,
                                 TipoContrato contrato, String correo, String telefono) {
        validarDatosBasicos(apellidoPaterno, apellidoMaterno, nombres, dni, rol, salario);
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombres = nombres;
        this.dni = dni;
        this.rol = rol;
        this.area = area;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.contrato = contrato;
        this.correo = correo;
        this.telefono = telefono;
    }

    /** Reactiva a un colaborador inactivo. */
    public void reactivar() {
        if (estaActivo()) {
            throw new BadRequestException("El colaborador ya se encuentra activo");
        }
        this.estado = EstadoEmpleado.ACTIVO;
        limpiarBajaProgramada();
    }

    /** Baja directa: el colaborador no tiene turnos activos vigentes. */
    public void darDeBajaSinTurnosActivos() {
        if (!estaActivo()) {
            throw new BadRequestException("El colaborador ya se encuentra inactivo");
        }
        this.estado = EstadoEmpleado.INACTIVO;
        limpiarBajaProgramada();
    }

    /**
     * Baja inmediata: el colaborador tenía turnos activos, pero se decide
     * inactivarlo de todas formas cancelando el turno más próximo. Requiere motivo.
     */
    public void darDeBajaInmediata(String motivo) {
        if (!estaActivo()) {
            throw new BadRequestException("El colaborador ya se encuentra inactivo");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException("Debe ingresar un motivo para la baja inmediata");
        }
        this.estado = EstadoEmpleado.INACTIVO;
        limpiarBajaProgramada();
    }

    /**
     * Programa la baja para la fecha/hora en que finaliza el último turno
     * activo del colaborador; el estado permanece Activo hasta esa fecha.
     */
    public void programarBaja(LocalDateTime fechaEfectiva, String observacion, String turnoInfo) {
        if (!estaActivo()) {
            throw new BadRequestException("El colaborador ya se encuentra inactivo");
        }
        if (fechaEfectiva == null) {
            throw new BadRequestException("La fecha efectiva de la baja programada es obligatoria");
        }
        this.bajaProgramadaFechaEfectiva = fechaEfectiva;
        this.bajaProgramadaObservacion = observacion;
        this.bajaProgramadaTurnoInfo = turnoInfo;
    }

    /**
     * Evaluado por el job de monitoreo automático: si corresponde, ejecuta la
     * baja programada. Devuelve true si el estado cambió.
     */
    public boolean ejecutarBajaProgramadaSiCorresponde(LocalDateTime ahora) {
        if (!estaActivo() || !tieneBajaProgramada()) {
            return false;
        }
        if (ahora.isBefore(bajaProgramadaFechaEfectiva)) {
            return false;
        }
        this.estado = EstadoEmpleado.INACTIVO;
        limpiarBajaProgramada();
        return true;
    }

    private void limpiarBajaProgramada() {
        this.bajaProgramadaFechaEfectiva = null;
        this.bajaProgramadaObservacion = null;
        this.bajaProgramadaTurnoInfo = null;
    }
}
