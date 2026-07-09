export class Venta {
  constructor({ id, clienteId, nombreCliente, estado, tipoComprobante, numeroComprobante, detalles, fecha, metodoPago }) {
    this.id            = id;
    this.clienteId     = clienteId ;
    this.nombreCliente = nombreCliente;
    this.fecha       = fecha       ?? null;
    this.estado      = estado       ?? null;
    this.metodoPago  =metodoPago;
    this.tipoComprobante =tipoComprobante;
    this.numeroComprobante =numeroComprobante;
    this.detalles       = detalles      ?? [detalleVenta];
  }
  

  static fromApi(raw) {
    return new Venta({
      id:         raw.id,
      clienteId:  raw.clienteId   ?? raw.cliente_id,
      nombreCliente:     raw.nombreCliente,
      fecha:      raw.fecha,
      estado:   raw.estado,
      metodoPago: raw.metodoPago  ?? raw.metodo_pago,
      tipoComprobante:raw.tipoComprobante,
      numeroComprobante:raw.numeroComprobante,
      detalles:raw.detalles
    });
  }

   static toApi(formData) {
  return {
    clienteId:       formData.clienteId       ?? null,
    metodoPago:      formData.metodoPago      ?? 'EFECTIVO',
    tipoComprobante: formData.tipoComprobante ?? 'BOLETA',
    detalles:        (formData.detalles ?? []).map((d) => ({
      medicamentoId: d.productoId, // ← productoId → medicamentoId
      cantidad:      d.cantidad,
    })),
  };
}
}
export class detalleVenta{
    constructor({medicamentoId,NombreMedicamento,cantidad,precioUnitario,subtotal}){
        this.medicamentoId           = medicamentoId;
        this.NombreMedicamento   = NombreMedicamento ;
        this.cantidad =cantidad;
        this.precioUnitario=precioUnitario;
        this.subtotal=subtotal;
    }
}