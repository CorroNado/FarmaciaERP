export const ROLES_EMPLEADO = {
  QUIMICO_FARMACEUTICO: 'Químico Farmacéutico',
  TECNICO_FARMACIA: 'Técnico en Farmacia',
  CAJA_ATENCION_CLIENTE: 'Caja y Atención al Cliente',
  ALMACEN: 'Almacén',
  ADMINISTRACION: 'Administración',
};

export const CONTRATOS_EMPLEADO = {
  INDEFINIDO: 'Indefinido',
  TEMPORAL: 'Temporal',
  PRACTICAS: 'Prácticas',
  MEDIO_TIEMPO: 'Medio Tiempo',
};

export const MOVIMIENTOS_RRHH = {
  ALTA: 'Alta',
  EDICION: 'Edición',
  CAMBIO_ESTADO: 'Cambio Estado',
  BAJA_INMEDIATA: 'Baja inmediata',
  BAJA_PROGRAMADA: 'Baja programada',
  CAMBIO_AUTOMATICO: 'Cambio automático de estado',
  ELIMINACION: 'Eliminación',
};

export class Empleado {
  constructor({
    id, codigo, apellidoPaterno, apellidoMaterno, nombres, nombreCompleto,
    dni, rol, area, fechaIngreso, salario, contrato, correo, telefono, estado,
    bajaProgramadaFechaEfectiva, bajaProgramadaObservacion, bajaProgramadaTurnoInfo,
  }) {
    this.id = id;
    this.codigo = codigo ?? '';
    this.apellidoPaterno = apellidoPaterno ?? '';
    this.apellidoMaterno = apellidoMaterno ?? '';
    this.nombres = nombres ?? '';
    this.nombreCompleto = nombreCompleto ?? `${apellidoPaterno ?? ''} ${apellidoMaterno ?? ''} ${nombres ?? ''}`.trim();
    this.dni = dni ?? '';
    this.rol = rol ?? 'QUIMICO_FARMACEUTICO';
    this.area = area ?? '';
    this.fechaIngreso = fechaIngreso ?? '';
    this.salario = salario ?? 0;
    this.contrato = contrato ?? 'INDEFINIDO';
    this.correo = correo ?? '';
    this.telefono = telefono ?? '';
    this.estado = estado ?? 'ACTIVO';
    this.bajaProgramadaFechaEfectiva = bajaProgramadaFechaEfectiva ?? null;
    this.bajaProgramadaObservacion = bajaProgramadaObservacion ?? '';
    this.bajaProgramadaTurnoInfo = bajaProgramadaTurnoInfo ?? '';
  }

  get estaActivo() {
    return this.estado === 'ACTIVO';
  }

  get tieneBajaProgramada() {
    return !!this.bajaProgramadaFechaEfectiva;
  }

  static fromApi(raw) {
    return new Empleado(raw);
  }

  // CrearEmpleadoRequest
  static toApiCreate(formData) {
    return {
      apellidoPaterno: formData.apellidoPaterno,
      apellidoMaterno: formData.apellidoMaterno,
      nombres: formData.nombres,
      dni: formData.dni,
      rol: formData.rol,
      area: formData.area,
      fechaIngreso: formData.fechaIngreso || null,
      salario: Number(formData.salario) || 0,
      contrato: formData.contrato,
      correo: formData.correo,
      telefono: formData.telefono,
    };
  }

  // ActualizarEmpleadoRequest (misma forma que crear)
  static toApiUpdate(formData) {
    return Empleado.toApiCreate(formData);
  }
}

export class EmpleadoAuditLog {
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
    return new EmpleadoAuditLog(raw);
  }
}
