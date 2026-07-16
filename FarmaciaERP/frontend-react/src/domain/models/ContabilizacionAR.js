// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoContabilizacionAR
export const ESTADO_CONTABILIZACION_AR = {
  INICIADA: 'INICIADA',
  ASIENTO_PROCESADO: 'ASIENTO_PROCESADO',
  RECETAS_EN_OBSERVACION: 'RECETAS_EN_OBSERVACION',
  RECETAS_CONFORMES: 'RECETAS_CONFORMES',
  CONSOLIDADA: 'CONSOLIDADA',
};

export const ESTADO_CONTABILIZACION_AR_LABEL = {
  [ESTADO_CONTABILIZACION_AR.INICIADA]: 'Contabilización iniciada — conciliación de lotes POS pendiente',
  [ESTADO_CONTABILIZACION_AR.ASIENTO_PROCESADO]: 'Asiento automatizado de ventas y cuadraturas contabilizado',
  [ESTADO_CONTABILIZACION_AR.RECETAS_EN_OBSERVACION]: 'Auditoría detectó recetas con observación',
  [ESTADO_CONTABILIZACION_AR.RECETAS_CONFORMES]: 'Firmas, troqueles y vigencia de recetas conformes',
  [ESTADO_CONTABILIZACION_AR.CONSOLIDADA]: 'Lote consolidado y valija física despachada',
};

export class ContabilizacionAR {
  constructor({
    id, cierreCajaId, cierreCajaNumero, fecha, tieneVariacion,
    conciliacionPOS, asientoProcesado, ajusteDescuadre,
    recetasAuditadas, recetasCorrectas, motivoObservacion,
    subsanacion, consolidado, estado, puedeContinuarFase03,
  }) {
    this.id = id;
    this.cierreCajaId = cierreCajaId ?? null;
    this.cierreCajaNumero = cierreCajaNumero ?? '';
    this.fecha = fecha ?? null;
    this.tieneVariacion = Boolean(tieneVariacion);
    this.conciliacionPOS = Boolean(conciliacionPOS);
    this.asientoProcesado = Boolean(asientoProcesado);
    this.ajusteDescuadre = Boolean(ajusteDescuadre);
    this.recetasAuditadas = Boolean(recetasAuditadas);
    this.recetasCorrectas = recetasCorrectas ?? null;
    this.motivoObservacion = motivoObservacion ?? '';
    this.subsanacion = Boolean(subsanacion);
    this.consolidado = Boolean(consolidado);
    this.estado = estado ?? null;
    this.puedeContinuarFase03 = Boolean(puedeContinuarFase03);
  }

  // 2.1.1 - solo aplica cuando el cierre tuvo variación de arqueo
  get requiereConciliacionPOS() {
    return this.tieneVariacion && !this.conciliacionPOS;
  }

  // 2.2.3 - ajuste de asientos descuadrados, solo si hubo variación
  get requiereAjusteDescuadre() {
    return this.tieneVariacion && this.asientoProcesado && !this.ajusteDescuadre;
  }

  get listaParaAuditoria() {
    return this.asientoProcesado && (!this.tieneVariacion || this.ajusteDescuadre);
  }

  get recetasObservadasPendientes() {
    return this.recetasCorrectas === false && !this.subsanacion;
  }

  get duplicadoPendienteReintento() {
    return this.recetasCorrectas === false && this.subsanacion;
  }

  get recetasConformes() {
    return this.recetasCorrectas === true;
  }

  static fromApi(raw) {
    return new ContabilizacionAR(raw);
  }

  // IniciarContabilizacionARRequest — { cierreCajaId }
  static toApiIniciar({ cierreCajaId }) {
    return {
      cierreCajaId: Number(cierreCajaId),
    };
  }

  // AuditarRecetasRequest — { conforme, motivoObservacion }
  static toApiAuditar({ conforme, motivoObservacion }) {
    return {
      conforme: Boolean(conforme),
      motivoObservacion: (motivoObservacion ?? '').trim(),
    };
  }
}
