// Espejo del backend: FarmaciaERP.Domain.Enums.ResultadoConciliacion
export const RESULTADO_CONCILIACION = {
  MATCH_OK: 'MATCH_OK',
  BLOQUEADO_MRBR: 'BLOQUEADO_MRBR',
};

export const RESULTADO_CONCILIACION_LABEL = {
  [RESULTADO_CONCILIACION.MATCH_OK]: 'Match exitoso — lista para programación de pago',
  [RESULTADO_CONCILIACION.BLOQUEADO_MRBR]: 'Bloqueada — pendiente de revisión en MRBR',
};

export class ConciliacionTresVias {
  constructor({
    id, numero, ordenCompraId, numeroOrdenCompra, razonSocialProveedor,
    entradaMercanciaId, numeroEntradaMercancia, facturaMIROId, numeroFactura,
    cantidadCoincide, precioCoincide, facturaVinculada, qaAprobado,
    resultado, fecha,
  }) {
    this.id = id;
    this.numero = numero ?? '';
    this.ordenCompraId = ordenCompraId ?? null;
    this.numeroOrdenCompra = numeroOrdenCompra ?? '';
    this.razonSocialProveedor = razonSocialProveedor ?? '';
    this.entradaMercanciaId = entradaMercanciaId ?? null;
    this.numeroEntradaMercancia = numeroEntradaMercancia ?? '';
    this.facturaMIROId = facturaMIROId ?? null;
    this.numeroFactura = numeroFactura ?? '';
    this.cantidadCoincide = Boolean(cantidadCoincide);
    this.precioCoincide = Boolean(precioCoincide);
    this.facturaVinculada = Boolean(facturaVinculada);
    this.qaAprobado = Boolean(qaAprobado);
    this.resultado = resultado ?? null;
    this.fecha = fecha ?? null;
  }

  get matchOk() {
    return this.resultado === RESULTADO_CONCILIACION.MATCH_OK;
  }

  get bloqueada() {
    return this.resultado === RESULTADO_CONCILIACION.BLOQUEADO_MRBR;
  }

  static fromApi(raw) {
    return new ConciliacionTresVias(raw);
  }

  // EjecutarConciliacionTresViasRequest — { ordenCompraId }
  static toApiEjecutar({ ordenCompraId }) {
    return {
      ordenCompraId: Number(ordenCompraId),
    };
  }
}
