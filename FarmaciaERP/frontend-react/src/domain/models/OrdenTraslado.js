// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoOrdenTraslado
export const ESTADO_STO = {
  EN_TRANSITO: 'EN_TRANSITO',
  RECIBIDO: 'RECIBIDO',
};

export class OrdenTraslado {
  constructor({
    id, numero, inspeccionCalidadId, lote,
    sucursalDestinoId, sucursalDestinoNombre, guiaRemision,
    estado, fechaDespacho, fechaRecepcion,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.inspeccionCalidadId = inspeccionCalidadId ?? null;
    this.lote = lote ?? '';
    this.sucursalDestinoId = sucursalDestinoId ?? null;
    this.sucursalDestinoNombre = sucursalDestinoNombre ?? '';
    this.guiaRemision = guiaRemision ?? '';
    this.estado = estado ?? ESTADO_STO.EN_TRANSITO;
    this.fechaDespacho = fechaDespacho ?? null;
    this.fechaRecepcion = fechaRecepcion ?? null;
  }

  // RN-E6-008: mientras la sucursal no confirme recepción en POS, el stock
  // permanece "en tránsito" y no está disponible para venta.
  get enTransito() {
    return this.estado === ESTADO_STO.EN_TRANSITO;
  }

  get recibido() {
    return this.estado === ESTADO_STO.RECIBIDO;
  }

  static fromApi(raw) {
    return new OrdenTraslado(raw);
  }

  // GenerarOrdenTrasladoRequest — { inspeccionCalidadId, sucursalDestinoId, guiaRemision }
  static toApiGenerar({ inspeccionCalidadId, sucursalDestinoId, guiaRemision }) {
    return {
      inspeccionCalidadId: Number(inspeccionCalidadId),
      sucursalDestinoId: Number(sucursalDestinoId),
      guiaRemision: (guiaRemision ?? '').trim(),
    };
  }
}
