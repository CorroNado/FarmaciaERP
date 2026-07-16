// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoLotePago
export const ESTADO_LOTE_PAGO = {
  EN_PREPARACION: 'EN_PREPARACION',
  PENDIENTE_APROBACION: 'PENDIENTE_APROBACION',
  CON_OBSERVACIONES: 'CON_OBSERVACIONES',
  APROBADO: 'APROBADO',
  CONCILIADO_GESTION: 'CONCILIADO_GESTION',
};

export const ESTADO_LOTE_PAGO_LABEL = {
  [ESTADO_LOTE_PAGO.EN_PREPARACION]: 'En preparación — priorización, negociación y armado del lote (F110)',
  [ESTADO_LOTE_PAGO.PENDIENTE_APROBACION]: 'Fondos verificados — pendiente de someter al Comité Semanal de Tesorería',
  [ESTADO_LOTE_PAGO.CON_OBSERVACIONES]: 'Con observaciones del Comité — requiere corrección antes de volver a someterlo',
  [ESTADO_LOTE_PAGO.APROBADO]: 'Aprobado por el Comité Semanal de Tesorería',
  [ESTADO_LOTE_PAGO.CONCILIADO_GESTION]: 'Pagos ejecutados y conciliados a nivel de gestión — apto para Fase 05',
};

export class LotePagoTesoreria {
  constructor({
    id, numero, detalles, montoNetoRegularizado,
    proveedoresCriticosPriorizados, descuentoProntoPagoNegociado, descuentoProntoPagoPct,
    lotePreparado, montoLote, fondosVerificados, revisionesComite, loteCorregido,
    aprobadoPorComite, pagosConciliadosGestion, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.detalles = Array.isArray(detalles) ? detalles : [];
    this.montoNetoRegularizado = montoNetoRegularizado ?? 0;
    this.proveedoresCriticosPriorizados = Boolean(proveedoresCriticosPriorizados);
    this.descuentoProntoPagoNegociado = Boolean(descuentoProntoPagoNegociado);
    this.descuentoProntoPagoPct = descuentoProntoPagoPct ?? 0;
    this.lotePreparado = Boolean(lotePreparado);
    this.montoLote = montoLote ?? 0;
    this.fondosVerificados = Boolean(fondosVerificados);
    this.revisionesComite = revisionesComite ?? 0;
    this.loteCorregido = Boolean(loteCorregido);
    this.aprobadoPorComite = Boolean(aprobadoPorComite);
    this.pagosConciliadosGestion = Boolean(pagosConciliadosGestion);
    this.estado = estado ?? null;
    this.fecha = fecha ?? null;
  }

  // 4.1 — pendiente de priorizar proveedores críticos de medicamentos
  get pendientePriorizar() {
    return !this.proveedoresCriticosPriorizados;
  }

  // 4.2 — priorizado, pendiente de negociar descuento por pronto pago
  get pendienteNegociar() {
    return this.proveedoresCriticosPriorizados && !this.descuentoProntoPagoNegociado;
  }

  // 4.3 — descuento negociado, pendiente de preparar el lote (F110)
  get pendientePreparar() {
    return this.descuentoProntoPagoNegociado && !this.lotePreparado;
  }

  // 4.4 — lote preparado, pendiente de verificar fondos
  get pendienteVerificarFondos() {
    return this.lotePreparado && !this.fondosVerificados;
  }

  // 4.4 (cont.) — fondos verificados, pendiente de someter al comité (primera vez)
  get pendienteSometerComite() {
    return this.fondosVerificados && !this.aprobadoPorComite && this.revisionesComite === 0;
  }

  // 4.4 (cont.) — con observaciones, pendiente de corregir
  get pendienteCorregir() {
    return this.estado === ESTADO_LOTE_PAGO.CON_OBSERVACIONES && !this.loteCorregido;
  }

  // 4.4 (cont.) — corregido, pendiente de volver a someter al comité
  get pendienteReSometer() {
    return this.loteCorregido && !this.aprobadoPorComite && this.revisionesComite >= 1;
  }

  // 4.5 — aprobado, pendiente de ejecutar pagos y conciliar
  get pendienteConciliar() {
    return this.aprobadoPorComite && !this.pagosConciliadosGestion;
  }

  static fromApi(raw) {
    return new LotePagoTesoreria(raw);
  }

  // IniciarLotePagoRequest — { ajusteContableIds: number[] }
  static toApiIniciar({ ajusteContableIds }) {
    return {
      ajusteContableIds: (ajusteContableIds ?? []).map(Number),
    };
  }

  // NegociarDescuentoProntoPagoRequest — { descuentoPct }
  static toApiNegociarDescuento({ descuentoPct }) {
    return {
      descuentoPct: Number(descuentoPct),
    };
  }
}
