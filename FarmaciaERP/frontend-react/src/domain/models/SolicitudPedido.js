export class SugerenciaMRPItem {
  constructor({ medicamentoId, nombreMedicamento, stockActual, stockMinimo, cantidadSugerida, precioUnitario, porDebajoDelMinimo }) {
    this.medicamentoId = medicamentoId;
    this.nombreMedicamento = nombreMedicamento ?? '';
    this.stockActual = Number(stockActual ?? 0);
    this.stockMinimo = Number(stockMinimo ?? 0);
    this.cantidadSugerida = Number(cantidadSugerida ?? 0);
    this.precioUnitario = Number(precioUnitario ?? 0);
    this.porDebajoDelMinimo = Boolean(porDebajoDelMinimo);
  }

  get subtotal() {
    return this.cantidadSugerida * this.precioUnitario;
  }

  static fromApi(raw) {
    return new SugerenciaMRPItem(raw);
  }
}

export class DetalleSolPed {
  constructor({ medicamentoId, nombreMedicamento, stockActual, stockMinimo, cantidadSugerida, precioUnitario, subtotal }) {
    this.medicamentoId = medicamentoId;
    this.nombreMedicamento = nombreMedicamento ?? '';
    this.stockActual = Number(stockActual ?? 0);
    this.stockMinimo = Number(stockMinimo ?? 0);
    this.cantidadSugerida = Number(cantidadSugerida ?? 0);
    this.precioUnitario = Number(precioUnitario ?? 0);
    this.subtotal = Number(subtotal ?? this.cantidadSugerida * this.precioUnitario);
  }

  static fromApi(raw) {
    return new DetalleSolPed(raw);
  }

  // DetalleSolPedRequest — el backend recalcula stockActual/precio desde el maestro
  static toApi(item) {
    return {
      medicamentoId: Number(item.medicamentoId),
      stockMinimo: Number(item.stockMinimo),
      cantidadSugerida: Number(item.cantidadSugerida),
    };
  }
}

export class SolicitudPedido {
  constructor({
    id, numero, fecha, responsable, centroCosto, presupuesto, detalles,
    total, estado, proveedorId, razonSocialProveedor, convenioId, numeroConvenio, motivoRechazo,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.fecha = fecha ?? null;
    this.responsable = responsable ?? '';
    this.centroCosto = centroCosto ?? '';
    this.presupuesto = Number(presupuesto ?? 0);
    this.detalles = (detalles ?? []).map(DetalleSolPed.fromApi);
    this.total = Number(total ?? 0);
    this.estado = estado ?? 'LIBERADA';
    this.proveedorId = proveedorId ?? null;
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.convenioId = convenioId ?? null;
    this.numeroConvenio = numeroConvenio ?? '';
    this.motivoRechazo = motivoRechazo ?? '';
  }

  get saldo() {
    return this.presupuesto - this.total;
  }

  get excedePresupuesto() {
    return this.saldo < 0;
  }

  get fuenteAprobada() {
    return this.estado === 'FUENTE_APROBADA' || this.estado === 'CONVERTIDA_OC';
  }

  get puedeAprobarFuente() {
    return this.estado === 'LIBERADA';
  }

  get puedeRechazar() {
    return this.estado === 'LIBERADA' || this.estado === 'FUENTE_APROBADA';
  }

  static fromApi(raw) {
    return new SolicitudPedido(raw);
  }

  // CrearSolPedRequest
  static toApiCreate(formData) {
    return {
      responsable: formData.responsable,
      centroCosto: formData.centroCosto,
      presupuesto: Number(formData.presupuesto),
      detalles: (formData.detalles ?? []).map(DetalleSolPed.toApi),
    };
  }
}
