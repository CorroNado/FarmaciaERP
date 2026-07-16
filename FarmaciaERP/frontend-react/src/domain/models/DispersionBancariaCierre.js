// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoDispersionCierre
export const ESTADO_DISPERSION_CIERRE = {
  EN_COMPILACION: 'EN_COMPILACION',
  PENDIENTE_VALIDACION: 'PENDIENTE_VALIDACION',
  CON_OBSERVACIONES: 'CON_OBSERVACIONES',
  VALIDADA: 'VALIDADA',
  ARCHIVO_GENERADO: 'ARCHIVO_GENERADO',
  FIRMADA: 'FIRMADA',
  TRANSFERIDA: 'TRANSFERIDA',
  EXTRACTO_IMPORTADO: 'EXTRACTO_IMPORTADO',
  CONCLUIDA: 'CONCLUIDA',
};

export const ESTADO_DISPERSION_CIERRE_LABEL = {
  [ESTADO_DISPERSION_CIERRE.EN_COMPILACION]: 'En compilación — pendiente de compilar la propuesta de pago (F110) recibida de la Fase 05',
  [ESTADO_DISPERSION_CIERRE.PENDIENTE_VALIDACION]: 'Propuesta compilada — pendiente de validar duplicados y bloqueos',
  [ESTADO_DISPERSION_CIERRE.CON_OBSERVACIONES]: 'Con observaciones — posible transferencia duplicada, requiere corrección',
  [ESTADO_DISPERSION_CIERRE.VALIDADA]: 'Propuesta validada — sin duplicados ni bloqueos',
  [ESTADO_DISPERSION_CIERRE.ARCHIVO_GENERADO]: 'Archivo bancario plano (IDoc) generado — pendiente de firma digital',
  [ESTADO_DISPERSION_CIERRE.FIRMADA]: 'Firmada digitalmente con token bancario — pendiente de transferencias',
  [ESTADO_DISPERSION_CIERRE.TRANSFERIDA]: 'Transferencias ejecutadas en banca empresa — pendiente de extracto bancario',
  [ESTADO_DISPERSION_CIERRE.EXTRACTO_IMPORTADO]: 'Extracto bancario digital importado (FF.5) — pendiente de conciliación',
  [ESTADO_DISPERSION_CIERRE.CONCLUIDA]: 'Cuentas puente conciliadas — obligación con proveedor extinguida (ciclo FI-AP finalizado)',
};

export class DispersionBancariaCierre {
  constructor({
    id, numero, propuestaPagoAutomaticaId, numeroPropuestaPago, montoDispersion,
    propuestaCompilada, propuestaValidada, intentosValidacion, loteCorregido,
    archivoGenerado, firmado, transferenciasEjecutadas, extractoImportado,
    conciliado, obligacionExtinguida, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.propuestaPagoAutomaticaId = propuestaPagoAutomaticaId ?? null;
    this.numeroPropuestaPago = numeroPropuestaPago ?? '';
    this.montoDispersion = montoDispersion ?? 0;
    this.propuestaCompilada = Boolean(propuestaCompilada);
    this.propuestaValidada = propuestaValidada ?? null;
    this.intentosValidacion = intentosValidacion ?? 0;
    this.loteCorregido = Boolean(loteCorregido);
    this.archivoGenerado = Boolean(archivoGenerado);
    this.firmado = Boolean(firmado);
    this.transferenciasEjecutadas = Boolean(transferenciasEjecutadas);
    this.extractoImportado = Boolean(extractoImportado);
    this.conciliado = Boolean(conciliado);
    this.obligacionExtinguida = Boolean(obligacionExtinguida);
    this.estado = estado ?? null;
    this.fecha = fecha ?? null;
  }

  // 6.1 — pendiente de compilar la propuesta de pago (F110) recibida de la Fase 05
  get pendienteCompilar() {
    return !this.propuestaCompilada;
  }

  // 6.2 — propuesta compilada, pendiente de validar duplicados/bloqueos
  get pendienteValidar() {
    return this.propuestaCompilada && this.propuestaValidada === null;
  }

  // 6.2 (cont.) — con observaciones, pendiente de corregir y reenviar el lote
  get pendienteCorregirReenviar() {
    return this.propuestaValidada === false;
  }

  // 6.3 — validada, pendiente de generar el archivo bancario plano (IDoc)
  get pendienteGenerarArchivo() {
    return this.propuestaValidada === true && !this.archivoGenerado;
  }

  // 6.4 — archivo generado, pendiente de firma digital con token bancario
  get pendienteFirmar() {
    return this.archivoGenerado && !this.firmado;
  }

  // 6.5 — firmada, pendiente de ejecutar transferencias en banca empresa
  get pendienteTransferir() {
    return this.firmado && !this.transferenciasEjecutadas;
  }

  // 6.6 — transferida, pendiente de importar el extracto bancario digital (FF.5)
  get pendienteImportarExtracto() {
    return this.transferenciasEjecutadas && !this.extractoImportado;
  }

  // 6.7 — extracto importado, pendiente de conciliar cuentas puente y compensar
  get pendienteConciliar() {
    return this.extractoImportado && !this.conciliado;
  }

  static fromApi(raw) {
    return new DispersionBancariaCierre(raw);
  }

  // IniciarDispersionBancariaRequest — { propuestaPagoAutomaticaId }
  static toApiIniciar({ propuestaPagoAutomaticaId }) {
    return {
      propuestaPagoAutomaticaId: Number(propuestaPagoAutomaticaId),
    };
  }
}
