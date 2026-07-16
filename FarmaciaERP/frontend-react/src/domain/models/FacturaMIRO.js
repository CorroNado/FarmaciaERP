// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoFacturaMIRO
export const ESTADO_MIRO = {
  REGISTRADA: 'REGISTRADA',
  ANULADA: 'ANULADA',
};

export class FacturaMIRO {
  constructor({
    id, numero, numeroFactura, ordenCompraId, numeroOrdenCompra, razonSocialProveedor,
    fechaEmision, montoNeto, igv, montoTotal, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.numeroFactura = numeroFactura ?? '';
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.fechaEmision = fechaEmision ?? '';
    this.montoNeto = Number(montoNeto ?? 0);
    this.igv = Number(igv ?? 0);
    this.montoTotal = Number(montoTotal ?? 0);
    this.estado = estado ?? ESTADO_MIRO.REGISTRADA;
    this.fecha = fecha ?? null;
  }

  get registrada() {
    return this.estado === ESTADO_MIRO.REGISTRADA;
  }

  static fromApi(raw) {
    return new FacturaMIRO(raw);
  }

  // RegistrarFacturaMIRORequest — { ordenCompraId, numeroFactura, fechaEmision }
  static toApiRegistrar({ ordenCompraId, numeroFactura, fechaEmision }) {
    return {
      ordenCompraId: Number(ordenCompraId),
      numeroFactura: (numeroFactura ?? '').trim(),
      fechaEmision,
    };
  }

  // Estimación previa en UI (el backend recalcula el total real desde la OC)
  static calcularTotales(montoNeto) {
    const neto = Number(montoNeto) || 0;
    const igv = neto * 0.18;
    return { neto, igv, total: neto + igv };
  }
}
