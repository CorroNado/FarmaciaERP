// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoEntradaMercancia
export const ESTADO_MIGO = {
  REGISTRADA: 'REGISTRADA', // en cuarentena, conforme con la OC
  SUSPENDIDA: 'SUSPENDIDA', // diferencia excede tolerancia (excepción documentada)
};

const TOLERANCIA_PCT = 2;
const TEMP_MIN = 2;
const TEMP_MAX = 8;

export class EntradaMercancia {
  constructor({
    id, numero, ordenCompraId, numeroOrdenCompra, razonSocialProveedor,
    lote, fechaVencimiento, temperaturaArribo,
    cantidadPedida, cantidadRecibida, diferencia, porcentajeDiferencia,
    estado, alertaCadenaFrio, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.lote = lote ?? '';
    this.fechaVencimiento = fechaVencimiento ?? '';
    this.temperaturaArribo = Number(temperaturaArribo ?? 0);
    this.cantidadPedida = Number(cantidadPedida ?? 0);
    this.cantidadRecibida = Number(cantidadRecibida ?? 0);
    this.diferencia = Number(diferencia ?? (this.cantidadRecibida - this.cantidadPedida));
    this.porcentajeDiferencia = Number(porcentajeDiferencia ?? 0);
    this.estado = estado ?? ESTADO_MIGO.REGISTRADA;
    this.alertaCadenaFrio = Boolean(alertaCadenaFrio);
    this.fecha = fecha ?? null;
  }

  // RN-F04-003: tolerancia del 2% entre lo pedido y lo recibido
  get dentroDeTolerancia() {
    return this.porcentajeDiferencia <= TOLERANCIA_PCT;
  }

  get suspendida() {
    return this.estado === ESTADO_MIGO.SUSPENDIDA;
  }

  static fromApi(raw) {
    return new EntradaMercancia(raw);
  }

  // RegistrarEntradaMercanciaRequest — { ordenCompraId, lote, fechaVencimiento,
  // temperaturaArribo, cantidadRecibida, confirmarExcepcion }
  static toApiRegistrar({ ordenCompraId, lote, fechaVencimiento, temperaturaArribo, cantidadRecibida, confirmarExcepcion }) {
    return {
      ordenCompraId: Number(ordenCompraId),
      lote: (lote ?? '').trim(),
      fechaVencimiento,
      temperaturaArribo: Number(temperaturaArribo),
      cantidadRecibida: Number(cantidadRecibida),
      confirmarExcepcion: Boolean(confirmarExcepcion),
    };
  }

  // Cálculo previo en UI, antes de enviar al backend (que es quien decide el estado final)
  static calcularDiferencia(cantidadPedida, cantidadRecibida) {
    const pedida = Number(cantidadPedida) || 0;
    const recibida = Number(cantidadRecibida) || 0;
    const dif = recibida - pedida;
    const pct = pedida ? (Math.abs(dif) / pedida) * 100 : 0;
    return { dif, pct, dentroDeTolerancia: pct <= TOLERANCIA_PCT };
  }

  static temperaturaFueraDeRango(temp) {
    const t = Number(temp);
    return t < TEMP_MIN || t > TEMP_MAX;
  }
}
