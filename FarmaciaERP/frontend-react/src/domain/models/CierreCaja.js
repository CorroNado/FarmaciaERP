// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoCierreCaja
export const ESTADO_CIERRE_CAJA = {
  ABIERTO: 'ABIERTO',
  CUADRADO: 'CUADRADO',
  CON_VARIACION: 'CON_VARIACION',
  JUSTIFICADO: 'JUSTIFICADO',
  LIBERADO: 'LIBERADO',
  CLASIFICADO: 'CLASIFICADO',
};

export const ESTADO_CIERRE_CAJA_LABEL = {
  [ESTADO_CIERRE_CAJA.ABIERTO]: 'Cierre emitido — arqueo pendiente',
  [ESTADO_CIERRE_CAJA.CUADRADO]: 'Arqueo cuadrado al 100%',
  [ESTADO_CIERRE_CAJA.CON_VARIACION]: 'Con variación — requiere justificación',
  [ESTADO_CIERRE_CAJA.JUSTIFICADO]: 'Descuadre justificado — físicos pendientes',
  [ESTADO_CIERRE_CAJA.LIBERADO]: 'Habilitado para clasificación automática',
  [ESTADO_CIERRE_CAJA.CLASIFICADO]: 'Copagos y coberturas clasificados',
};

export class CierreCaja {
  constructor({
    id, numero, sucursalId, sucursalNombre, fecha, reporteVentas,
    arqueo, diferencia, cuadra, justificacion, fisicosEnviados,
    copago, coberturaAseg, estado, puedeContinuarFase02,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.sucursalId = sucursalId ?? null;
    this.sucursalNombre = sucursalNombre ?? '';
    this.fecha = fecha ?? null;
    this.reporteVentas = reporteVentas ?? 0;
    this.arqueo = arqueo ?? null;
    this.diferencia = diferencia ?? 0;
    this.cuadra = cuadra ?? null;
    this.justificacion = justificacion ?? '';
    this.fisicosEnviados = Boolean(fisicosEnviados);
    this.copago = copago ?? 0;
    this.coberturaAseg = coberturaAseg ?? 0;
    this.estado = estado ?? null;
    this.puedeContinuarFase02 = Boolean(puedeContinuarFase02);
  }

  get arqueoRegistrado() {
    return this.arqueo !== null && this.arqueo !== undefined;
  }

  get tieneVariacion() {
    return this.cuadra === false;
  }

  get requiereJustificacion() {
    return this.tieneVariacion && !this.justificacion;
  }

  get requiereEnvioFisicos() {
    return this.tieneVariacion && !!this.justificacion && !this.fisicosEnviados;
  }

  get habilitadoParaClasificar() {
    return this.cuadra === true || (this.tieneVariacion && this.fisicosEnviados);
  }

  get clasificado() {
    return this.copago > 0;
  }

  static fromApi(raw) {
    return new CierreCaja(raw);
  }

  // AbrirCierreCajaRequest — { numero, sucursalId, reporteVentas }
  static toApiAbrir({ numero, sucursalId, reporteVentas }) {
    return {
      numero: (numero ?? '').trim(),
      sucursalId: Number(sucursalId),
      reporteVentas: Number(reporteVentas),
    };
  }

  // RegistrarArqueoRequest — { montoContado }
  static toApiArqueo({ montoContado }) {
    return {
      montoContado: Number(montoContado),
    };
  }

  // RegistrarJustificacionRequest — { justificacion }
  static toApiJustificacion({ justificacion }) {
    return {
      justificacion: (justificacion ?? '').trim(),
    };
  }
}
