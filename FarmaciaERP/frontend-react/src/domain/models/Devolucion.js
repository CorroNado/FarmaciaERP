export class Devolucion {
  constructor({
    id, ventaId, fecha, motivo, accion, detalles, monto, venta,
  }) {
    this.id = id;
    this.ventaId = ventaId;
    this.fecha = fecha ?? null;
    this.motivo = motivo ?? null;
    this.accion = accion ?? null;
    this.detalles = (detalles ?? []).map((d) => ({
      medicamentoId: d.medicamentoId,
      nombreMedicamento: d.nombreMedicamento,
      cantidad: d.cantidad,
      precioUnitario: Number(d.precioUnitario ?? 0),
      subtotal: Number(d.subtotal ?? 0),
    }));
    this.monto = Number(monto ?? 0);
    this.venta = venta ?? null;
  }

  static fromApi(raw) {
    return new Devolucion(raw);
  }

  // Arma el payload que espera CrearDevolucionRequest a partir de los ítems
  // seleccionados de una venta ya cobrada.
  static toApiCrear({ ventaId, motivo, accion, items }) {
    return {
      ventaId,
      motivo,
      accion,
      detalles: items.map((item) => ({
        medicamentoId: item.medicamentoId,
        cantidad: item.cantidad,
      })),
    };
  }
}

export const MOTIVOS_DEVOLUCION = [
  { value: 'PRODUCTO_DEFECTUOSO', label: 'Producto defectuoso' },
  { value: 'ERROR_DESPACHO', label: 'Error de despacho' },
  { value: 'CLIENTE_INSATISFECHO', label: 'Cliente insatisfecho' },
  { value: 'PRODUCTO_PROXIMO_VENCER', label: 'Producto próximo a vencer' },
  { value: 'OTRO', label: 'Otro' },
];

export const ACCIONES_DEVOLUCION = [
  { value: 'NOTA_CREDITO', label: 'Nota de crédito' },
  { value: 'REEMBOLSO_EFECTIVO', label: 'Reembolso efectivo' },
  { value: 'CAMBIO_PRODUCTO', label: 'Cambio de producto' },
];
