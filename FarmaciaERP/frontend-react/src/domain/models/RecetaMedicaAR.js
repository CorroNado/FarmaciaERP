// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoRecetaAR
export const ESTADO_RECETA_AR = {
  PENDIENTE: 'PENDIENTE',
  VALIDANDO: 'VALIDANDO',
  RECHAZADA: 'RECHAZADA',
  APROBADA: 'APROBADA',
  IMPUGNANDO: 'IMPUGNANDO',
  LIBERADA: 'LIBERADA',
  DEBITO: 'DEBITO',
};

export const ESTADO_RECETA_AR_LABEL = {
  [ESTADO_RECETA_AR.PENDIENTE]: 'Pendiente',
  [ESTADO_RECETA_AR.VALIDANDO]: 'En validación',
  [ESTADO_RECETA_AR.RECHAZADA]: 'Rechazada',
  [ESTADO_RECETA_AR.APROBADA]: 'Aprobada',
  [ESTADO_RECETA_AR.IMPUGNANDO]: 'Impugnación enviada',
  [ESTADO_RECETA_AR.LIBERADA]: 'Liberada para cobro',
  [ESTADO_RECETA_AR.DEBITO]: 'Débito confirmado',
};

// Tono visual por estado — usado por el badge/card de la receta
export const ESTADO_RECETA_AR_TONE = {
  [ESTADO_RECETA_AR.PENDIENTE]: 'default',
  [ESTADO_RECETA_AR.VALIDANDO]: 'warn',
  [ESTADO_RECETA_AR.RECHAZADA]: 'err',
  [ESTADO_RECETA_AR.APROBADA]: 'ok',
  [ESTADO_RECETA_AR.IMPUGNANDO]: 'warn',
  [ESTADO_RECETA_AR.LIBERADA]: 'ok',
  [ESTADO_RECETA_AR.DEBITO]: 'err',
};

const ESTADOS_TERMINALES = [
  ESTADO_RECETA_AR.RECHAZADA,
  ESTADO_RECETA_AR.APROBADA,
  ESTADO_RECETA_AR.LIBERADA,
  ESTADO_RECETA_AR.DEBITO,
];

export class RecetaMedicaAR {
  constructor({
    id, numero, contabilizacionARId, medicamento, aseguradora,
    montoDeclarado, montoPreliquidado, estado, motivoRechazo,
    inconsistencia, fecha, procesada, generaDebito,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.contabilizacionARId = contabilizacionARId ?? null;
    this.medicamento = medicamento ?? '';
    this.aseguradora = aseguradora ?? '';
    this.montoDeclarado = montoDeclarado ?? 0;
    this.montoPreliquidado = montoPreliquidado ?? 0;
    this.estado = estado ?? ESTADO_RECETA_AR.PENDIENTE;
    this.motivoRechazo = motivoRechazo ?? '';
    this.inconsistencia = inconsistencia ?? '';
    this.fecha = fecha ?? null;
    this.procesada = Boolean(procesada);
    this.generaDebito = Boolean(generaDebito);
  }

  get esTerminal() {
    return ESTADOS_TERMINALES.includes(this.estado);
  }

  get requiereValidacion() {
    return this.estado === ESTADO_RECETA_AR.PENDIENTE;
  }

  get requiereComparacion() {
    return this.estado === ESTADO_RECETA_AR.VALIDANDO;
  }

  get requiereRespuestaAseguradora() {
    return this.estado === ESTADO_RECETA_AR.IMPUGNANDO;
  }

  static fromApi(raw) {
    return new RecetaMedicaAR(raw);
  }

  // RegistrarRecetaMedicaARRequest — { numero, contabilizacionARId, medicamento, aseguradora, montoDeclarado, montoPreliquidado }
  static toApiRegistrar({ numero, contabilizacionARId, medicamento, aseguradora, montoDeclarado, montoPreliquidado }) {
    return {
      numero: (numero ?? '').trim(),
      contabilizacionARId: Number(contabilizacionARId),
      medicamento: (medicamento ?? '').trim(),
      aseguradora: (aseguradora ?? '').trim(),
      montoDeclarado: Number(montoDeclarado),
      montoPreliquidado: Number(montoPreliquidado),
    };
  }

  // ValidarTroquelesFirmasRequest — { valido, motivoRechazo }
  static toApiValidar({ valido, motivoRechazo }) {
    return {
      valido: Boolean(valido),
      motivoRechazo: (motivoRechazo ?? '').trim(),
    };
  }

  // CompararPreliquidacionRequest — { coincide, inconsistencia }
  static toApiComparar({ coincide, inconsistencia }) {
    return {
      coincide: Boolean(coincide),
      inconsistencia: (inconsistencia ?? '').trim(),
    };
  }

  // RegistrarRespuestaAseguradoraRequest — { aceptaImpugnacion }
  static toApiRespuesta({ aceptaImpugnacion }) {
    return {
      aceptaImpugnacion: Boolean(aceptaImpugnacion),
    };
  }
}
