export const ESTADO_OC = {
  BORRADOR: 'BORRADOR',
  FIRMADA: 'FIRMADA',
  ANULADA: 'ANULADA',
};

export class DetalleOrdenCompra {
  constructor({ medicamentoId, nombreMedicamento, cantidad, precioUnitario, subtotal }) {
    this.medicamentoId = medicamentoId;
    this.nombreMedicamento = nombreMedicamento ?? '';
    this.cantidad = Number(cantidad ?? 0);
    this.precioUnitario = Number(precioUnitario ?? 0);
    this.subtotal = Number(subtotal ?? this.cantidad * this.precioUnitario);
  }

  static fromApi(raw) {
    return new DetalleOrdenCompra(raw);
  }
}

export class OrdenCompra {
  constructor({
    id, numero, solPedId, numeroSolPed, proveedorId, razonSocialProveedor,
    convenioId, detalles, montoTotal, fecha, fechaEntregaLimite, centroDestino,
    estado, fechaFirma,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.solPedId = solPedId ?? null;
    this.numeroSolPed = numeroSolPed ?? '';
    this.proveedorId = proveedorId ?? null;
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.convenioId = convenioId ?? null;
    this.detalles = (detalles ?? []).map(DetalleOrdenCompra.fromApi);
    this.montoTotal = Number(montoTotal ?? 0);
    this.fecha = fecha ?? null;
    this.fechaEntregaLimite = fechaEntregaLimite ?? '';
    this.centroDestino = centroDestino ?? '';
    this.estado = estado ?? ESTADO_OC.BORRADOR;
    this.fechaFirma = fechaFirma ?? null;
  }

  // RN-OC-002: precio y proveedor bloqueados (heredados del Info-Record / convenio)
  get puedeFirmar() {
    return this.estado === ESTADO_OC.BORRADOR;
  }

  get firmada() {
    return this.estado === ESTADO_OC.FIRMADA;
  }

  static fromApi(raw) {
    return new OrdenCompra(raw);
  }

  // CrearOrdenCompraRequest — { solPedId, centroDestino }
  static toApiCreate({ solPedId, centroDestino }) {
    return {
      solPedId: Number(solPedId),
      centroDestino,
    };
  }
}
