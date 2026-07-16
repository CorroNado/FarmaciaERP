export class ItemConvenio {
  constructor({ medicamentoId, nombreMedicamento, precioPactado }) {
    this.medicamentoId = medicamentoId;
    this.nombreMedicamento = nombreMedicamento ?? '';
    this.precioPactado = Number(precioPactado ?? 0);
  }

  static fromApi(raw) {
    return new ItemConvenio({
      medicamentoId: raw.medicamentoId,
      nombreMedicamento: raw.nombreMedicamento,
      precioPactado: raw.precioPactado,
    });
  }

  // ItemConvenioRequest
  static toApi(item) {
    return {
      medicamentoId: Number(item.medicamentoId),
      precioPactado: Number(item.precioPactado),
    };
  }
}

export class Convenio {
  constructor({ id, numero, proveedorId, razonSocialProveedor, fechaInicio, fechaFin, estado, vigente, itemsPactados }) {
    this.id = id;
    this.numero = numero ?? '';
    this.proveedorId = proveedorId;
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.fechaInicio = fechaInicio ?? '';
    this.fechaFin = fechaFin ?? '';
    this.estado = estado ?? 'VIGENTE';
    this.vigente = Boolean(vigente);
    this.itemsPactados = (itemsPactados ?? []).map(ItemConvenio.fromApi);
  }

  static fromApi(raw) {
    return new Convenio({
      id: raw.id,
      numero: raw.numero,
      proveedorId: raw.proveedorId,
      razonSocialProveedor: raw.razonSocialProveedor,
      fechaInicio: raw.fechaInicio,
      fechaFin: raw.fechaFin,
      estado: raw.estado,
      vigente: raw.vigente,
      itemsPactados: raw.itemsPactados,
    });
  }

  // CrearConvenioRequest
  static toApiCreate(formData) {
    return {
      numero: formData.numero,
      proveedorId: Number(formData.proveedorId),
      fechaInicio: formData.fechaInicio,
      fechaFin: formData.fechaFin,
      itemsPactados: (formData.itemsPactados ?? []).map(ItemConvenio.toApi),
    };
  }
}
