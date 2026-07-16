// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoDebitoAR
export const ESTADO_DEBITO_AR = {
  PENDIENTE_JUSTIFICACION: 'PENDIENTE_JUSTIFICACION',
  JUSTIFICADO: 'JUSTIFICADO',
  NO_JUSTIFICADO: 'NO_JUSTIFICADO',
  RECLAMO_TRAMITADO: 'RECLAMO_TRAMITADO',
  AJUSTADO: 'AJUSTADO',
};

export const ESTADO_DEBITO_AR_LABEL = {
  [ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION]: 'Pendiente de justificación',
  [ESTADO_DEBITO_AR.JUSTIFICADO]: 'Justificado',
  [ESTADO_DEBITO_AR.NO_JUSTIFICADO]: 'No justificado',
  [ESTADO_DEBITO_AR.RECLAMO_TRAMITADO]: 'Reclamo tramitado',
  [ESTADO_DEBITO_AR.AJUSTADO]: 'Ajustado',
};

export const ESTADO_DEBITO_AR_TONE = {
  [ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION]: 'default',
  [ESTADO_DEBITO_AR.JUSTIFICADO]: 'ok',
  [ESTADO_DEBITO_AR.NO_JUSTIFICADO]: 'err',
  [ESTADO_DEBITO_AR.RECLAMO_TRAMITADO]: 'warn',
  [ESTADO_DEBITO_AR.AJUSTADO]: 'ok',
};

export class DebitoAR {
  constructor({
    id, recetaMedicaARId, numeroReceta, contabilizacionARId, monto, motivo,
    estado, justificado, tramitado, ajustado, fecha, fechaAjuste, conciliado,
  }) {
    this.id = id;
    this.recetaMedicaARId = recetaMedicaARId ?? null;
    this.numeroReceta = numeroReceta ?? '';
    this.contabilizacionARId = contabilizacionARId ?? null;
    this.monto = monto ?? 0;
    this.motivo = motivo ?? '';
    this.estado = estado ?? ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION;
    this.justificado = justificado ?? null;
    this.tramitado = Boolean(tramitado);
    this.ajustado = Boolean(ajustado);
    this.fecha = fecha ?? null;
    this.fechaAjuste = fechaAjuste ?? null;
    this.conciliado = Boolean(conciliado);
  }

  get requiereEvaluacionJustificacion() {
    return this.estado === ESTADO_DEBITO_AR.PENDIENTE_JUSTIFICACION;
  }

  get requiereTramitarReclamo() {
    return this.estado === ESTADO_DEBITO_AR.NO_JUSTIFICADO && !this.tramitado;
  }

  get requiereAjusteTecnico() {
    return !this.ajustado
      && (this.estado === ESTADO_DEBITO_AR.JUSTIFICADO || this.estado === ESTADO_DEBITO_AR.RECLAMO_TRAMITADO);
  }

  static fromApi(raw) {
    return new DebitoAR(raw);
  }

  // CalcularDebitoARRequest — { recetaMedicaARId }
  static toApiCalcular({ recetaMedicaARId }) {
    return { recetaMedicaARId: Number(recetaMedicaARId) };
  }

  // EvaluarJustificacionDebitoARRequest — { justificado }
  static toApiEvaluarJustificacion({ justificado }) {
    return { justificado: Boolean(justificado) };
  }
}
