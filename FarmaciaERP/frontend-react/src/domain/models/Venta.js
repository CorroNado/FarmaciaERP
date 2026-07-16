export const IGV_RATE = 0.18;

export class Venta {
  constructor({
    id, clienteId, nombreCliente, fecha, estado,
    metodoPago, tipoComprobante, numeroComprobante, detalles, total,
  }) {
    this.id = id;
    this.clienteId = clienteId;
    this.nombreCliente = nombreCliente ?? '';
    this.fecha = fecha ?? null;
    this.estado = estado ?? 'PENDIENTE';
    this.metodoPago = metodoPago ?? null;
    this.tipoComprobante = tipoComprobante ?? 'NINGUNO';
    this.numeroComprobante = numeroComprobante ?? null;
    this.detalles = (detalles ?? []).map((d) => ({
      medicamentoId: d.medicamentoId,
      nombreMedicamento: d.nombreMedicamento,
      cantidad: d.cantidad,
      precioUnitario: Number(d.precioUnitario ?? 0),
      subtotal: Number(d.subtotal ?? 0),
    }));
    this.total = Number(total ?? 0);
  }

  static fromApi(raw) {
    return new Venta(raw);
  }

  // Arma el payload que espera CrearVentaRequest a partir del carrito del POS
  static toApiCrear({ clienteId, metodoPago, tipoComprobante, cart }) {
    return {
      clienteId,
      metodoPago,
      tipoComprobante,
      detalles: cart.map((item) => ({
        medicamentoId: item.id,
        cantidad: item.cantidad,
      })),
    };
  }
}

// Calcula subtotal / IGV informativo / total a partir de las líneas del carrito.
// El total oficial de la venta lo define el backend (suma de precio*cantidad).
export function calcularTotales(cart) {
  const subtotal = cart.reduce((acc, i) => acc + i.precio * i.cantidad, 0);
  const igv = subtotal - subtotal / (1 + IGV_RATE);
  return { subtotal: subtotal - igv, igv, total: subtotal };
}
