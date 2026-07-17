export const MESES_PLANILLA = {
  1: 'Enero', 2: 'Febrero', 3: 'Marzo', 4: 'Abril', 5: 'Mayo', 6: 'Junio',
  7: 'Julio', 8: 'Agosto', 9: 'Septiembre', 10: 'Octubre', 11: 'Noviembre', 12: 'Diciembre',
};

export const MES_OPTIONS = Object.entries(MESES_PLANILLA).map(([value, label]) => ({ value, label }));

export class DetallePlanillaEmpleado {
  constructor({
    empleadoId, codigoEmpleado, nombreCompleto, sueldoBase, sueldoDiario,
    diasConTurno, faltas, minutosTardanza, horasExtras, montoExtra,
    descuentoFaltas, descuentoTardanzas, bonoAsistencia, bonoCumplimiento,
    bonoMetas, essalud, afp, otrosDescuentos, sueldoNeto,
  }) {
    this.empleadoId = empleadoId;
    this.codigoEmpleado = codigoEmpleado ?? '';
    this.nombreCompleto = nombreCompleto ?? '';
    this.sueldoBase = sueldoBase ?? 0;
    this.sueldoDiario = sueldoDiario ?? 0;
    this.diasConTurno = diasConTurno ?? 0;
    this.faltas = faltas ?? 0;
    this.minutosTardanza = minutosTardanza ?? 0;
    this.horasExtras = horasExtras ?? 0;
    this.montoExtra = montoExtra ?? 0;
    this.descuentoFaltas = descuentoFaltas ?? 0;
    this.descuentoTardanzas = descuentoTardanzas ?? 0;
    this.bonoAsistencia = bonoAsistencia ?? 0;
    this.bonoCumplimiento = bonoCumplimiento ?? 0;
    this.bonoMetas = bonoMetas ?? 0;
    this.essalud = essalud ?? 0;
    this.afp = afp ?? 0;
    this.otrosDescuentos = otrosDescuentos ?? 0;
    this.sueldoNeto = sueldoNeto ?? 0;
  }

  static fromApi(raw) {
    return new DetallePlanillaEmpleado(raw);
  }
}

export class Planilla {
  constructor({ id, mes, nombreMes, anio, guardada, detalles, montoTotalNeto, fechaCalculo, fechaGuardado }) {
    this.id = id ?? null;
    this.mes = mes;
    this.nombreMes = nombreMes ?? MESES_PLANILLA[mes] ?? '';
    this.anio = anio;
    this.guardada = guardada ?? false;
    this.detalles = (detalles ?? []).map(DetallePlanillaEmpleado.fromApi);
    this.montoTotalNeto = montoTotalNeto ?? 0;
    this.fechaCalculo = fechaCalculo ?? null;
    this.fechaGuardado = fechaGuardado ?? null;
  }

  static fromApi(raw) {
    return new Planilla(raw);
  }

  // GuardarPlanillaRequest
  static toApiGuardar({ mes, anio, bonosMetas = [], confirmarSobrescritura = false }) {
    return {
      mes: Number(mes),
      anio: Number(anio),
      bonosMetas: bonosMetas.map((b) => ({ empleadoId: Number(b.empleadoId), bonoMetas: Number(b.bonoMetas) || 0 })),
      confirmarSobrescritura,
    };
  }
}

export class PlanillaResumen {
  constructor({ id, mes, nombreMes, anio, cantidadColaboradores, montoTotalNeto, fechaGuardado }) {
    this.id = id;
    this.mes = mes;
    this.nombreMes = nombreMes ?? MESES_PLANILLA[mes] ?? '';
    this.anio = anio;
    this.cantidadColaboradores = cantidadColaboradores ?? 0;
    this.montoTotalNeto = montoTotalNeto ?? 0;
    this.fechaGuardado = fechaGuardado ?? null;
  }

  static fromApi(raw) {
    return new PlanillaResumen(raw);
  }
}
