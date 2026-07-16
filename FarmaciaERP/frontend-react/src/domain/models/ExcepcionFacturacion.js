// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoExcepcionFacturacion
export const ESTADO_EXCEPCION_FACTURACION = {
  BLOQUEADA: 'BLOQUEADA',
  REVISADA: 'REVISADA',
  NOTIFICADA: 'NOTIFICADA',
};

export const ESTADO_EXCEPCION_FACTURACION_LABEL = {
  [ESTADO_EXCEPCION_FACTURACION.BLOQUEADA]: 'Bloqueada — pendiente de revisión',
  [ESTADO_EXCEPCION_FACTURACION.REVISADA]: 'Revisada — pendiente de clasificación',
  [ESTADO_EXCEPCION_FACTURACION.NOTIFICADA]: 'Clasificada y notificada a Compras',
};

// Espejo del backend: FarmaciaERP.Domain.Enums.TipoDiscrepancia
export const TIPO_DISCREPANCIA = {
  PRECIO: 'PRECIO',
  CANTIDAD: 'CANTIDAD',
};

export const TIPO_DISCREPANCIA_LABEL = {
  [TIPO_DISCREPANCIA.PRECIO]: 'Precio — desviación frente al vademécum / convenio',
  [TIPO_DISCREPANCIA.CANTIDAD]: 'Cantidad — desviación frente a la cantidad recibida (MIGO)',
};

export class ExcepcionFacturacion {
  constructor({
    id, numero, conciliacionTresViasId, numeroConciliacion, ordenCompraId, numeroOrdenCompra,
    razonSocialProveedor, facturaMIROId, numeroFactura, montoFactura, montoContrato, diferencia,
    tipoDiscrepancia, estado, revisada, clasificada, notificada, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.conciliacionTresViasId = conciliacionTresViasId ?? null;
    this.numeroConciliacion = numeroConciliacion ?? '';
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.facturaMIROId = facturaMIROId ?? null;
    this.numeroFactura = numeroFactura ?? '';
    this.montoFactura = montoFactura ?? 0;
    this.montoContrato = montoContrato ?? 0;
    this.diferencia = diferencia ?? 0;
    this.tipoDiscrepancia = tipoDiscrepancia ?? null;
    this.estado = estado ?? null;
    this.revisada = Boolean(revisada);
    this.clasificada = Boolean(clasificada);
    this.notificada = Boolean(notificada);
    this.fecha = fecha ?? null;
  }

  get pendienteRevision() {
    return !this.revisada;
  }

  get pendienteClasificacion() {
    return this.revisada && !this.clasificada;
  }

  static fromApi(raw) {
    return new ExcepcionFacturacion(raw);
  }

  // CapturarExcepcionFacturacionRequest — { conciliacionTresViasId }
  static toApiCapturar({ conciliacionTresViasId }) {
    return {
      conciliacionTresViasId: Number(conciliacionTresViasId),
    };
  }

  // ClasificarExcepcionFacturacionRequest — { tipoDiscrepancia }
  static toApiClasificar({ tipoDiscrepancia }) {
    return {
      tipoDiscrepancia,
    };
  }
}
