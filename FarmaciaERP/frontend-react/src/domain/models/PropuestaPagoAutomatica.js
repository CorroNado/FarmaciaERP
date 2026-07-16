// Espejo del backend: FarmaciaERP.Domain.Enums.EstadoPropuestaPago
export const ESTADO_PROPUESTA_PAGO = {
  EN_PARAMETRIZACION: 'EN_PARAMETRIZACION',
  PROPUESTA_EJECUTADA: 'PROPUESTA_EJECUTADA',
  CON_EXCEPCIONES: 'CON_EXCEPCIONES',
  APROBADA: 'APROBADA',
  PAGO_EJECUTADO: 'PAGO_EJECUTADO',
  CONCLUIDA: 'CONCLUIDA',
};

export const ESTADO_PROPUESTA_PAGO_LABEL = {
  [ESTADO_PROPUESTA_PAGO.EN_PARAMETRIZACION]: 'En parametrización — pendiente de introducir parámetros de pago (F110)',
  [ESTADO_PROPUESTA_PAGO.PROPUESTA_EJECUTADA]: 'Propuesta de pago ejecutada — pendiente de revisión de excepciones y bloqueos',
  [ESTADO_PROPUESTA_PAGO.CON_EXCEPCIONES]: 'Con excepciones — datos bancarios desactualizados, requiere ajustar parámetros y reejecutar',
  [ESTADO_PROPUESTA_PAGO.APROBADA]: 'Propuesta de pago final aprobada — pendiente de ejecución de pago',
  [ESTADO_PROPUESTA_PAGO.PAGO_EJECUTADO]: 'Pago ejecutado en SAP — pendiente de generar archivos bancarios',
  [ESTADO_PROPUESTA_PAGO.CONCLUIDA]: 'Archivos bancarios generados — apta para Fase 06 (Dispersión Bancaria y Conciliación)',
};

export class PropuestaPagoAutomatica {
  constructor({
    id, numero, lotePagoTesoreriaId, numeroLotePago, montoPropuesta,
    sociedad, viaPago, fechaPago, parametrosIntroducidos, propuestaEjecutada,
    intentos, propuestaCorrecta, propuestaAprobada, pagoEjecutado,
    archivosGenerados, estado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.lotePagoTesoreriaId = lotePagoTesoreriaId ?? null;
    this.numeroLotePago = numeroLotePago ?? '';
    this.montoPropuesta = montoPropuesta ?? 0;
    this.sociedad = sociedad ?? '';
    this.viaPago = viaPago ?? '';
    this.fechaPago = fechaPago ?? '';
    this.parametrosIntroducidos = Boolean(parametrosIntroducidos);
    this.propuestaEjecutada = Boolean(propuestaEjecutada);
    this.intentos = intentos ?? 0;
    this.propuestaCorrecta = propuestaCorrecta ?? null;
    this.propuestaAprobada = Boolean(propuestaAprobada);
    this.pagoEjecutado = Boolean(pagoEjecutado);
    this.archivosGenerados = Boolean(archivosGenerados);
    this.estado = estado ?? null;
    this.fecha = fecha ?? null;
  }

  // 5.1 — pendiente de introducir parámetros de pago en F110
  get pendienteParametros() {
    return !this.parametrosIntroducidos;
  }

  // 5.1 (cont.) — parámetros introducidos, pendiente de ejecutar la propuesta automática
  get pendienteEjecutarPropuesta() {
    return this.parametrosIntroducidos && !this.propuestaEjecutada;
  }

  // 5.2 — propuesta ejecutada, pendiente de revisar el reporte de excepciones
  get pendienteRevisarExcepciones() {
    return this.propuestaEjecutada && this.propuestaCorrecta === null;
  }

  // 5.2 (cont.) — con excepciones, pendiente de ajustar parámetros y reejecutar
  get pendienteAjustarReejecutar() {
    return this.propuestaCorrecta === false;
  }

  // 5.2 (cont.) — sin excepciones, pendiente de aprobar la propuesta final
  get pendienteAprobar() {
    return this.propuestaCorrecta === true && !this.propuestaAprobada;
  }

  // 5.3 — aprobada, pendiente de ejecutar el pago
  get pendienteEjecutarPago() {
    return this.propuestaAprobada && !this.pagoEjecutado;
  }

  // 5.3 (cont.) — pago ejecutado, pendiente de generar archivos bancarios
  get pendienteGenerarArchivos() {
    return this.pagoEjecutado && !this.archivosGenerados;
  }

  static fromApi(raw) {
    return new PropuestaPagoAutomatica(raw);
  }

  // IniciarPropuestaPagoRequest — { lotePagoTesoreriaId }
  static toApiIniciar({ lotePagoTesoreriaId }) {
    return {
      lotePagoTesoreriaId: Number(lotePagoTesoreriaId),
    };
  }

  // IntroducirParametrosPagoRequest — { sociedad, viaPago, fechaPago }
  static toApiParametros({ sociedad, viaPago, fechaPago }) {
    return { sociedad, viaPago, fechaPago };
  }
}
