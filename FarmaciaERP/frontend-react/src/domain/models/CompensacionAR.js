// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoCompensacionAR
export const ESTADO_COMPENSACION_AR = {
  COMPENSADO: 'COMPENSADO',
  REPORTE_GENERADO: 'REPORTE_GENERADO',
  SALDO_CONFIRMADO: 'SALDO_CONFIRMADO',
  CERRADO: 'CERRADO',
};

export const ESTADO_COMPENSACION_AR_LABEL = {
  [ESTADO_COMPENSACION_AR.COMPENSADO]: 'Compensado',
  [ESTADO_COMPENSACION_AR.REPORTE_GENERADO]: 'Reporte generado',
  [ESTADO_COMPENSACION_AR.SALDO_CONFIRMADO]: 'Saldo confirmado',
  [ESTADO_COMPENSACION_AR.CERRADO]: 'Cerrado',
};

export const ESTADO_COMPENSACION_AR_TONE = {
  [ESTADO_COMPENSACION_AR.COMPENSADO]: 'default',
  [ESTADO_COMPENSACION_AR.REPORTE_GENERADO]: 'warn',
  [ESTADO_COMPENSACION_AR.SALDO_CONFIRMADO]: 'warn',
  [ESTADO_COMPENSACION_AR.CERRADO]: 'ok',
};

export class CompensacionAR {
  constructor({
    id, contabilizacionARId, compensado, reporteGenerado, montoVentas,
    montoAprobadas, perdidas, margenNeto, margenPct, saldoConfirmado,
    cerrado, estado, fecha, fechaCierre, finalizado,
  }) {
    this.id = id;
    this.contabilizacionARId = contabilizacionARId ?? null;
    this.compensado = Boolean(compensado);
    this.reporteGenerado = Boolean(reporteGenerado);
    this.montoVentas = montoVentas ?? 0;
    this.montoAprobadas = montoAprobadas ?? 0;
    this.perdidas = perdidas ?? 0;
    this.margenNeto = margenNeto ?? 0;
    this.margenPct = margenPct ?? 0;
    this.saldoConfirmado = Boolean(saldoConfirmado);
    this.cerrado = Boolean(cerrado);
    this.estado = estado ?? ESTADO_COMPENSACION_AR.COMPENSADO;
    this.fecha = fecha ?? null;
    this.fechaCierre = fechaCierre ?? null;
    this.finalizado = Boolean(finalizado);
  }

  get requiereGenerarReporte() {
    return this.compensado && !this.reporteGenerado;
  }

  get requiereConfirmarSaldo() {
    return this.reporteGenerado && !this.saldoConfirmado;
  }

  get requiereCerrarIngresos() {
    return this.saldoConfirmado && !this.cerrado;
  }

  static fromApi(raw) {
    return new CompensacionAR(raw);
  }

  // AplicarCompensacionAutomaticaRequest — { contabilizacionARId }
  static toApiAplicar({ contabilizacionARId }) {
    return { contabilizacionARId: Number(contabilizacionARId) };
  }
}
