// Espejo del backend: FarmaciaERP.Domain.Enums.DecisionQA
export const DECISION_QA = {
  APROBADO: 'APROBADO',
  RECHAZADO: 'RECHAZADO',
};

export class InspeccionCalidad {
  constructor({
    id, numero, entradaMercanciaId, numeroEntradaMercancia, lote,
    muestreoConforme, cadenaFrioConforme, registroSanitarioVigente, empaqueConforme,
    decision, motivoRechazo, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.entradaMercanciaId = entradaMercanciaId ?? null;
    this.numeroEntradaMercancia = numeroEntradaMercancia ?? '';
    this.lote = lote ?? '';
    this.muestreoConforme = Boolean(muestreoConforme);
    this.cadenaFrioConforme = Boolean(cadenaFrioConforme);
    this.registroSanitarioVigente = Boolean(registroSanitarioVigente);
    this.empaqueConforme = Boolean(empaqueConforme);
    this.decision = decision ?? null;
    this.motivoRechazo = motivoRechazo ?? '';
    this.fecha = fecha ?? null;
  }

  get aprobado() {
    return this.decision === DECISION_QA.APROBADO;
  }

  get rechazado() {
    return this.decision === DECISION_QA.RECHAZADO;
  }

  // RN-E5-006: todos los controles deben estar conformes (incluida la cadena de frío,
  // que llega precalculada desde el MIGO) para habilitar la Decisión de Empleo.
  static todosConformes({ muestreoConforme, cadenaFrioConforme, registroSanitarioVigente, empaqueConforme }) {
    return Boolean(muestreoConforme) && Boolean(cadenaFrioConforme)
      && Boolean(registroSanitarioVigente) && Boolean(empaqueConforme);
  }

  static fromApi(raw) {
    return new InspeccionCalidad(raw);
  }

  // AprobarLoteRequest — { entradaMercanciaId, muestreoConforme, registroSanitarioVigente, empaqueConforme }
  static toApiAprobar({ entradaMercanciaId, muestreoConforme, registroSanitarioVigente, empaqueConforme }) {
    return {
      entradaMercanciaId: Number(entradaMercanciaId),
      muestreoConforme: Boolean(muestreoConforme),
      registroSanitarioVigente: Boolean(registroSanitarioVigente),
      empaqueConforme: Boolean(empaqueConforme),
    };
  }

  // RechazarLoteRequest — { entradaMercanciaId, motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme }
  static toApiRechazar({ entradaMercanciaId, motivoRechazo, muestreoConforme, registroSanitarioVigente, empaqueConforme }) {
    return {
      entradaMercanciaId: Number(entradaMercanciaId),
      motivoRechazo: (motivoRechazo ?? '').trim(),
      muestreoConforme: Boolean(muestreoConforme),
      registroSanitarioVigente: Boolean(registroSanitarioVigente),
      empaqueConforme: Boolean(empaqueConforme),
    };
  }
}
