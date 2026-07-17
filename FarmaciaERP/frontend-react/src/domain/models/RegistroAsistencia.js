export const TURNOS_ASISTENCIA = {
  MANANA: 'Mañana',
  TARDE: 'Tarde',
  NOCHE_GUARDIA: 'Noche / Guardia',
  MEDIO_TIEMPO: 'Medio Tiempo',
  APOYO_REFUERZO: 'Apoyo / Refuerzo',
};

export const ESTADOS_ASISTENCIA = {
  PROGRAMADO: 'Programado',
  PENDIENTE: 'Pendiente',
  A_TIEMPO: 'A tiempo',
  TARDANZA: 'Tardanza',
  FALTA_INJUSTIFICADA: 'Falta Injustificada',
  JUSTIFICADO: 'Justificado',
};

export const MOVIMIENTOS_ASISTENCIA = {
  PROGRAMACION: 'Programación de turno',
  CHECKIN: 'Checkin',
  CHECKOUT: 'Checkout',
  JUSTIFICACION: 'Justificación',
  EDICION: 'Edición',
  ELIMINACION: 'Eliminación',
  CAMBIO_AUTOMATICO: 'Cambio automático de estado',
};

export class RegistroAsistencia {
  constructor({
    id, empleadoId, codigoEmpleado, colaborador, rol, fecha, turno,
    horaEntrada, horaSalida, horasTrabajadas, horasExtras, factorExtra,
    estado, registrado, justificado, motivoJustificacion, fechaCreacion,
  }) {
    this.id = id;
    this.empleadoId = empleadoId;
    this.codigoEmpleado = codigoEmpleado ?? '';
    this.colaborador = colaborador ?? '';
    this.rol = rol ?? '';
    this.fecha = fecha ?? '';
    this.turno = turno ?? 'MANANA';
    this.horaEntrada = horaEntrada ?? null;
    this.horaSalida = horaSalida ?? null;
    this.horasTrabajadas = horasTrabajadas ?? null;
    this.horasExtras = horasExtras ?? null;
    this.factorExtra = factorExtra ?? null;
    this.estado = estado ?? 'PROGRAMADO';
    this.registrado = registrado ?? false;
    this.justificado = justificado ?? false;
    this.motivoJustificacion = motivoJustificacion ?? '';
    this.fechaCreacion = fechaCreacion ?? null;
  }

  get puedeMarcarEntrada() {
    return (this.estado === 'PROGRAMADO' || this.estado === 'PENDIENTE') && !this.registrado && !this.justificado;
  }

  get puedeMarcarSalida() {
    return this.registrado && !this.horaSalida && !this.justificado;
  }

  get puedeJustificar() {
    return !this.justificado && this.estado !== 'A_TIEMPO';
  }

  get puedeEditar() {
    return this.estado === 'A_TIEMPO' || this.estado === 'TARDANZA';
  }

  static fromApi(raw) {
    return new RegistroAsistencia(raw);
  }

  // ProgramarAsistenciaRequest
  static toApiProgramar(formData) {
    return {
      empleadoId: Number(formData.empleadoId),
      fecha: formData.fecha || null,
      turno: formData.turno,
    };
  }
}

export class AsistenciaAuditLog {
  constructor({ id, fecha, usuario, colaborador, codigoEmpleado, tipo, detalle, antes, despues, motivo }) {
    this.id = id;
    this.fecha = fecha;
    this.usuario = usuario ?? '';
    this.colaborador = colaborador ?? '';
    this.codigoEmpleado = codigoEmpleado ?? '';
    this.tipo = tipo ?? '';
    this.detalle = detalle ?? '';
    this.antes = antes ?? '';
    this.despues = despues ?? '';
    this.motivo = motivo ?? '';
  }

  static fromApi(raw) {
    return new AsistenciaAuditLog(raw);
  }
}
