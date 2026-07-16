// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoCobroAR
export const ESTADO_COBRO_AR = {
  INTERPRETADO: 'INTERPRETADO',
  CONCILIADO: 'CONCILIADO',
  DESCUADRADO: 'DESCUADRADO',
  AJUSTADO: 'AJUSTADO',
  REGISTRADO: 'REGISTRADO',
};

export const ESTADO_COBRO_AR_LABEL = {
  [ESTADO_COBRO_AR.INTERPRETADO]: 'Archivo interpretado',
  [ESTADO_COBRO_AR.CONCILIADO]: 'Conciliado',
  [ESTADO_COBRO_AR.DESCUADRADO]: 'Descuadrado',
  [ESTADO_COBRO_AR.AJUSTADO]: 'Ajustado',
  [ESTADO_COBRO_AR.REGISTRADO]: 'Registrado',
};

export const ESTADO_COBRO_AR_TONE = {
  [ESTADO_COBRO_AR.INTERPRETADO]: 'default',
  [ESTADO_COBRO_AR.CONCILIADO]: 'ok',
  [ESTADO_COBRO_AR.DESCUADRADO]: 'err',
  [ESTADO_COBRO_AR.AJUSTADO]: 'ok',
  [ESTADO_COBRO_AR.REGISTRADO]: 'ok',
};

export class CobroAR {
  constructor({
    id, contabilizacionARId, montoTransferido, retenciones, comisionPct,
    montoConciliado, diferencia, cuadra, registrado, estado, fecha,
    fechaRegistro, puedeContinuarFase06,
  }) {
    this.id = id;
    this.contabilizacionARId = contabilizacionARId ?? null;
    this.montoTransferido = montoTransferido ?? 0;
    this.retenciones = retenciones ?? 0;
    this.comisionPct = comisionPct ?? 0;
    this.montoConciliado = montoConciliado ?? 0;
    this.diferencia = diferencia ?? 0;
    this.cuadra = cuadra ?? null;
    this.registrado = Boolean(registrado);
    this.estado = estado ?? ESTADO_COBRO_AR.INTERPRETADO;
    this.fecha = fecha ?? null;
    this.fechaRegistro = fechaRegistro ?? null;
    this.puedeContinuarFase06 = Boolean(puedeContinuarFase06);
  }

  get requiereConciliarComisiones() {
    return this.estado === ESTADO_COBRO_AR.INTERPRETADO;
  }

  get requiereAjusteDiferencia() {
    return this.estado === ESTADO_COBRO_AR.DESCUADRADO;
  }

  get requiereRegistrarIngreso() {
    return this.cuadra === true && !this.registrado;
  }

  static fromApi(raw) {
    return new CobroAR(raw);
  }

  // InterpretarArchivoTransferenciaRequest — { contabilizacionARId, retenciones }
  static toApiInterpretar({ contabilizacionARId, retenciones }) {
    return {
      contabilizacionARId: Number(contabilizacionARId),
      retenciones: Number(retenciones),
    };
  }

  // ConciliarComisionesRetencionesRequest — { comisionPct }
  static toApiConciliar({ comisionPct }) {
    return { comisionPct: Number(comisionPct) };
  }
}
