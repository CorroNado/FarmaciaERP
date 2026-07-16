// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoPago
export const ESTADO_PAGO = {
  EJECUTADO: 'EJECUTADO',
  ANULADO: 'ANULADO',
};

export const ESTADO_PAGO_LABEL = {
  [ESTADO_PAGO.EJECUTADO]: 'Ejecutado — cuenta por pagar cerrada (FIPO)',
  [ESTADO_PAGO.ANULADO]: 'Anulado',
};

export class Pago {
  constructor({
    id, numero, facturaMIROId, numeroFactura, ordenCompraId, numeroOrdenCompra,
    razonSocialProveedor, conciliacionTresViasId, banco, fechaPago, monto,
    estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.facturaMIROId = facturaMIROId ?? null;
    this.numeroFactura = numeroFactura ?? '';
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.conciliacionTresViasId = conciliacionTresViasId ?? null;
    this.banco = banco ?? '';
    this.fechaPago = fechaPago ?? '';
    this.monto = Number(monto ?? 0);
    this.estado = estado ?? ESTADO_PAGO.EJECUTADO;
    this.fecha = fecha ?? null;
  }

  get ejecutado() {
    return this.estado === ESTADO_PAGO.EJECUTADO;
  }

  static fromApi(raw) {
    return new Pago(raw);
  }

  // EjecutarPagoRequest — { facturaMIROId, banco, fechaPago }
  static toApiEjecutar({ facturaMIROId, banco, fechaPago }) {
    return {
      facturaMIROId: Number(facturaMIROId),
      banco: (banco ?? '').trim(),
      fechaPago: (fechaPago ?? '').trim(),
    };
  }
}
