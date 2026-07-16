// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoDisputaComercial
export const ESTADO_DISPUTA_COMERCIAL = {
  EN_GESTION: 'EN_GESTION',
  RESUELTA: 'RESUELTA',
};

export const ESTADO_DISPUTA_COMERCIAL_LABEL = {
  [ESTADO_DISPUTA_COMERCIAL.EN_GESTION]: 'En gestión — cotejando, cuantificando y negociando la desviación',
  [ESTADO_DISPUTA_COMERCIAL.RESUELTA]: 'Resuelta en el workflow del ERP — apta para Fase 03',
};

export class DisputaComercial {
  constructor({
    id, numero, excepcionFacturacionId, numeroExcepcion, tipoDiscrepancia,
    ordenCompraId, numeroOrdenCompra, razonSocialProveedor,
    cotejada, cuantificada, impactoFinanciero, validadaDesviacion,
    disputaAbierta, rondaNegociacion, montoContraoferta, absorbeAceptado,
    resueltaWorkflow, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.excepcionFacturacionId = excepcionFacturacionId ?? null;
    this.numeroExcepcion = numeroExcepcion ?? '';
    this.tipoDiscrepancia = tipoDiscrepancia ?? null;
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.cotejada = Boolean(cotejada);
    this.cuantificada = Boolean(cuantificada);
    this.impactoFinanciero = impactoFinanciero ?? 0;
    this.validadaDesviacion = Boolean(validadaDesviacion);
    this.disputaAbierta = Boolean(disputaAbierta);
    this.rondaNegociacion = rondaNegociacion ?? 0;
    this.montoContraoferta = montoContraoferta ?? 0;
    this.absorbeAceptado = absorbeAceptado === undefined ? null : absorbeAceptado;
    this.resueltaWorkflow = Boolean(resueltaWorkflow);
    this.estado = estado ?? null;
    this.fecha = fecha ?? null;
  }

  // 2.1.1 — pendiente de cotejar factura vs. contrato
  get pendienteCotejo() {
    return !this.cotejada;
  }

  // 2.1.2 — cotejada, pendiente de cuantificar impacto financiero
  get pendienteCuantificacion() {
    return this.cotejada && !this.cuantificada;
  }

  // 2.1.3 — cuantificada, pendiente de validar la desviación
  get pendienteValidacion() {
    return this.cuantificada && !this.validadaDesviacion;
  }

  // 2.2.1 — validada, sin absorción aceptada aún y sin disputa abierta: puede abrirse nueva ronda
  get puedeAbrirNegociacion() {
    return this.validadaDesviacion && !this.resueltaWorkflow
      && !this.disputaAbierta && this.absorbeAceptado !== true;
  }

  // 2.2.1 (cont.) — disputa abierta, esperando contrapropuesta del proveedor
  get esperandoContraoferta() {
    return this.disputaAbierta && this.montoContraoferta <= 0;
  }

  // 2.2.2 — el proveedor ya envió contrapropuesta, falta decidir si se absorbe
  get pendienteEvaluacionAbsorcion() {
    return this.disputaAbierta && this.montoContraoferta > 0 && this.absorbeAceptado === null;
  }

  // 2.3.1 — absorción aceptada, falta registrar la resolución en el workflow
  get puedeResolverWorkflow() {
    return this.absorbeAceptado === true && !this.resueltaWorkflow;
  }

  static fromApi(raw) {
    return new DisputaComercial(raw);
  }

  // AbrirDisputaComercialRequest — { excepcionFacturacionId }
  static toApiAbrir({ excepcionFacturacionId }) {
    return {
      excepcionFacturacionId: Number(excepcionFacturacionId),
    };
  }

  // RegistrarContraofertaRequest — { montoContraoferta }
  static toApiContraoferta({ montoContraoferta }) {
    return {
      montoContraoferta: Number(montoContraoferta),
    };
  }
}
