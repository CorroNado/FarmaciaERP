// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoAjusteContable
export const ESTADO_AJUSTE_CONTABLE = {
  PENDIENTE_NOTA_CREDITO: 'PENDIENTE_NOTA_CREDITO',
  GESTION_RECLAMO: 'GESTION_RECLAMO',
  NOTA_CREDITO_REGISTRADA: 'NOTA_CREDITO_REGISTRADA',
  REGULARIZADA: 'REGULARIZADA',
};

export const ESTADO_AJUSTE_CONTABLE_LABEL = {
  [ESTADO_AJUSTE_CONTABLE.PENDIENTE_NOTA_CREDITO]: 'Pendiente — a la espera de confirmar la recepción de la Nota de Crédito',
  [ESTADO_AJUSTE_CONTABLE.GESTION_RECLAMO]: 'En gestión de reclamo — Nota de Crédito no recibida',
  [ESTADO_AJUSTE_CONTABLE.NOTA_CREDITO_REGISTRADA]: 'Nota de Crédito registrada en SAP — pendiente de asiento',
  [ESTADO_AJUSTE_CONTABLE.REGULARIZADA]: 'Regularizada — apta para Fase 04',
};

export class AjusteContableRegularizacion {
  constructor({
    id, numero, disputaComercialId, numeroDisputaComercial, excepcionFacturacionId, numeroExcepcion,
    razonSocialProveedor, montoRegularizacion, recibeNotaCredito, reclamoGestionado,
    notaCreditoEnviadaProveedor, notaCreditoRegistrada, asientoRegularizacion,
    desbloqueado, regularizada, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.disputaComercialId = disputaComercialId ?? null;
    this.numeroDisputaComercial = numeroDisputaComercial ?? '';
    this.excepcionFacturacionId = excepcionFacturacionId ?? null;
    this.numeroExcepcion = numeroExcepcion ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.montoRegularizacion = montoRegularizacion ?? 0;
    this.recibeNotaCredito = recibeNotaCredito === undefined ? null : recibeNotaCredito;
    this.reclamoGestionado = Boolean(reclamoGestionado);
    this.notaCreditoEnviadaProveedor = Boolean(notaCreditoEnviadaProveedor);
    this.notaCreditoRegistrada = Boolean(notaCreditoRegistrada);
    this.asientoRegularizacion = Boolean(asientoRegularizacion);
    this.desbloqueado = Boolean(desbloqueado);
    this.regularizada = Boolean(regularizada);
    this.estado = estado ?? null;
    this.fecha = fecha ?? null;
  }

  // 3.1 — pendiente de confirmar si se recibe Nota de Crédito
  get pendienteConfirmarNotaCredito() {
    return this.recibeNotaCredito === null;
  }

  // 3.1.a — NC no recibida, pendiente de gestionar el reclamo
  get pendienteGestionarReclamo() {
    return this.recibeNotaCredito === false && !this.reclamoGestionado;
  }

  // 3.1.a (cont.) — reclamo gestionado, pendiente de que el proveedor envíe la NC
  get pendienteEvaluarEnvioNotaCredito() {
    return this.recibeNotaCredito === false && this.reclamoGestionado && !this.notaCreditoEnviadaProveedor;
  }

  // 3.1.b — NC recibida directamente, pendiente de registrarla en SAP
  get pendienteRegistrarNotaCredito() {
    return this.recibeNotaCredito === true && !this.notaCreditoRegistrada;
  }

  // 3.2 — NC ya registrada (por cualquiera de los dos caminos), pendiente del asiento
  get pendienteAsientoRegularizacion() {
    return this.notaCreditoRegistrada && !this.asientoRegularizacion;
  }

  // 3.3 — asiento ejecutado, pendiente de desbloquear la partida
  get pendienteDesbloquearPartida() {
    return this.asientoRegularizacion && !this.desbloqueado;
  }

  static fromApi(raw) {
    return new AjusteContableRegularizacion(raw);
  }

  // IniciarAjusteContableRequest — { disputaComercialId }
  static toApiIniciar({ disputaComercialId }) {
    return {
      disputaComercialId: Number(disputaComercialId),
    };
  }

  // RegistrarRecepcionNotaCreditoRequest — { recibida }
  static toApiRecepcionNotaCredito({ recibida }) {
    return {
      recibida: Boolean(recibida),
    };
  }
}
