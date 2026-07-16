export class Cotizacion {
  constructor({
    id, clienteId, nombreCliente, fecha, vigenciaDias, fechaVencimiento,
    vigente, estado, motivoRechazo, ventaGeneradaId, detalles, total,
  }) {
    this.id = id;
    this.clienteId = clienteId;
    this.nombreCliente = nombreCliente ?? '';
    this.fecha = fecha ?? null;
    this.vigenciaDias = Number(vigenciaDias ?? 0);
    this.fechaVencimiento = fechaVencimiento ?? null;
    this.vigente = Boolean(vigente);
    this.estado = estado ?? 'PENDIENTE';
    this.motivoRechazo = motivoRechazo ?? null;
    this.ventaGeneradaId = ventaGeneradaId ?? null;
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
    return new Cotizacion(raw);
  }

  // Arma el payload que espera CrearCotizacionRequest a partir de los ítems seleccionados
  static toApiCrear({ clienteId, vigenciaDias, items }) {
    return {
      clienteId,
      vigenciaDias: Number(vigenciaDias),
      detalles: items.map((item) => ({
        medicamentoId: item.id,
        cantidad: item.cantidad,
      })),
    };
  }
}

export const MOTIVOS_RECHAZO_COTIZACION = [
  'Precio elevado',
  'Eligió otra farmacia',
  'Producto no disponible a tiempo',
  'Ya no lo requiere',
];

export const VIGENCIAS_COTIZACION = [
  { value: 7, label: '7 días' },
  { value: 15, label: '15 días' },
  { value: 30, label: '30 días' },
];
