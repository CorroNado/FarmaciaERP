export const ENDPOINTS = {
  auth: {
    login: '/auth/login',
  },
  users: {
    list:    '/usuario',
    create:  '/usuario',
    getById: (id) => `/usuario/${id}`,
    update:  (id) => `/usuario/${id}`,
    delete:  (id) => `/usuario/${id}`,
  },
  reports: {
    accessByUser: (id) => `/reports/access/${id}`,
  },
  medicamentos: {
    list: '/medicamento',
    getById: (id) => `/medicamento/${id}`,
  },
  clientes: {
    list: '/cliente',
    create: '/cliente',
    getById: (id) => `/cliente/${id}`,
  },
  ventas: {
    list: '/venta',
    create: '/venta',
    getById: (id) => `/venta/${id}`,
    pagar: (id) => `/venta/${id}/pagar`,
    anular: (id) => `/venta/${id}/anular`,
  },
  cotizaciones: {
    list: '/cotizacion',
    create: '/cotizacion',
    getById: (id) => `/cotizacion/${id}`,
    aceptar: (id) => `/cotizacion/${id}/aceptar`,
    rechazar: (id) => `/cotizacion/${id}/rechazar`,
  },
  devoluciones: {
    list: '/devolucion',
    create: '/devolucion',
    getById: (id) => `/devolucion/${id}`,
  },
  fiAp: {
    // FI-AP · Fase 01 — Captura de Excepciones de Facturación (Frontera Logística)
    excepcionesFacturacion: {
      list: '/fi-ap/excepciones-facturacion',
      capturar: '/fi-ap/excepciones-facturacion',
      getById: (id) => `/fi-ap/excepciones-facturacion/${id}`,
      revisar: (id) => `/fi-ap/excepciones-facturacion/${id}/revisar`,
      clasificar: (id) => `/fi-ap/excepciones-facturacion/${id}/clasificar`,
    },
    // FI-AP · Fase 02 — Gestión Humana de Disputas Comerciales (Workflow ERP)
    disputasComerciales: {
      list: '/fi-ap/disputas-comerciales',
      abrir: '/fi-ap/disputas-comerciales',
      getById: (id) => `/fi-ap/disputas-comerciales/${id}`,
      cotejar: (id) => `/fi-ap/disputas-comerciales/${id}/cotejar`,
      cuantificar: (id) => `/fi-ap/disputas-comerciales/${id}/cuantificar`,
      validarDesviacion: (id) => `/fi-ap/disputas-comerciales/${id}/validar-desviacion`,
      abrirNegociacion: (id) => `/fi-ap/disputas-comerciales/${id}/abrir-negociacion`,
      contraoferta: (id) => `/fi-ap/disputas-comerciales/${id}/contraoferta`,
      aceptarAbsorcion: (id) => `/fi-ap/disputas-comerciales/${id}/aceptar-absorcion`,
      reabrirNegociacion: (id) => `/fi-ap/disputas-comerciales/${id}/reabrir-negociacion`,
      resolverWorkflow: (id) => `/fi-ap/disputas-comerciales/${id}/resolver-workflow`,
    },
    // FI-AP · Fase 03 — Ajustes Contables y Regularización (Cierre de Transacción)
    ajustesContables: {
      list: '/fi-ap/ajustes-contables',
      iniciar: '/fi-ap/ajustes-contables',
      getById: (id) => `/fi-ap/ajustes-contables/${id}`,
      recepcionNotaCredito: (id) => `/fi-ap/ajustes-contables/${id}/recepcion-nota-credito`,
      gestionarReclamo: (id) => `/fi-ap/ajustes-contables/${id}/gestionar-reclamo`,
      evaluarEnvioNotaCredito: (id) => `/fi-ap/ajustes-contables/${id}/evaluar-envio-nota-credito`,
      registrarNotaCredito: (id) => `/fi-ap/ajustes-contables/${id}/registrar-nota-credito`,
      asientoRegularizacion: (id) => `/fi-ap/ajustes-contables/${id}/asiento-regularizacion`,
      desbloquearPartida: (id) => `/fi-ap/ajustes-contables/${id}/desbloquear-partida`,
    },
    // FI-AP · Fase 04 — Estrategia de Tesorería y Riesgo Sanitario (F110 SAP)
    lotesPago: {
      list: '/fi-ap/lotes-pago',
      iniciar: '/fi-ap/lotes-pago',
      getById: (id) => `/fi-ap/lotes-pago/${id}`,
      priorizar: (id) => `/fi-ap/lotes-pago/${id}/priorizar`,
      negociarDescuento: (id) => `/fi-ap/lotes-pago/${id}/negociar-descuento`,
      preparar: (id) => `/fi-ap/lotes-pago/${id}/preparar`,
      verificarFondos: (id) => `/fi-ap/lotes-pago/${id}/verificar-fondos`,
      someterComite: (id) => `/fi-ap/lotes-pago/${id}/someter-comite`,
      corregir: (id) => `/fi-ap/lotes-pago/${id}/corregir`,
      ejecutarConciliar: (id) => `/fi-ap/lotes-pago/${id}/ejecutar-conciliar`,
    },
    // FI-AP · Fase 05 — Procesamiento Automático y Propuesta de Pago (F110)
    propuestasPago: {
      list: '/fi-ap/propuestas-pago',
      iniciar: '/fi-ap/propuestas-pago',
      getById: (id) => `/fi-ap/propuestas-pago/${id}`,
      parametros: (id) => `/fi-ap/propuestas-pago/${id}/parametros`,
      ejecutarPropuesta: (id) => `/fi-ap/propuestas-pago/${id}/ejecutar-propuesta`,
      revisarExcepciones: (id) => `/fi-ap/propuestas-pago/${id}/revisar-excepciones`,
      ajustarReejecutar: (id) => `/fi-ap/propuestas-pago/${id}/ajustar-reejecutar`,
      aprobar: (id) => `/fi-ap/propuestas-pago/${id}/aprobar`,
      ejecutarPago: (id) => `/fi-ap/propuestas-pago/${id}/ejecutar-pago`,
      generarArchivos: (id) => `/fi-ap/propuestas-pago/${id}/generar-archivos`,
    },
    // FI-AP · Fase 06 — Dispersión Bancaria y Conciliación de Cierre (RN-AP6-01)
    dispersionesBancarias: {
      list: '/fi-ap/dispersiones-bancarias',
      iniciar: '/fi-ap/dispersiones-bancarias',
      getById: (id) => `/fi-ap/dispersiones-bancarias/${id}`,
      compilar: (id) => `/fi-ap/dispersiones-bancarias/${id}/compilar`,
      validar: (id) => `/fi-ap/dispersiones-bancarias/${id}/validar`,
      corregirReenviar: (id) => `/fi-ap/dispersiones-bancarias/${id}/corregir-reenviar`,
      generarArchivo: (id) => `/fi-ap/dispersiones-bancarias/${id}/generar-archivo`,
      firmar: (id) => `/fi-ap/dispersiones-bancarias/${id}/firmar`,
      transferir: (id) => `/fi-ap/dispersiones-bancarias/${id}/transferir`,
      importarExtracto: (id) => `/fi-ap/dispersiones-bancarias/${id}/importar-extracto`,
      conciliar: (id) => `/fi-ap/dispersiones-bancarias/${id}/conciliar`,
    },
  },
  fiAr: {
    // FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD)
    cierreCaja: {
      list: '/fi-ar/cierre-caja',
      abrir: '/fi-ar/cierre-caja',
      getById: (id) => `/fi-ar/cierre-caja/${id}`,
      arqueo: (id) => `/fi-ar/cierre-caja/${id}/arqueo`,
      justificacion: (id) => `/fi-ar/cierre-caja/${id}/justificacion`,
      enviarFisicosRecetas: (id) => `/fi-ar/cierre-caja/${id}/enviar-fisicos-recetas`,
      clasificarCopagoCobertura: (id) => `/fi-ar/cierre-caja/${id}/clasificar-copago-cobertura`,
    },
    // FI-AR · Fase 02 — Contabilización y Declaración de Valores (SAP FI-AR)
    contabilizacion: {
      list: '/fi-ar/contabilizacion',
      iniciar: '/fi-ar/contabilizacion',
      getById: (id) => `/fi-ar/contabilizacion/${id}`,
      conciliarLotesPOS: (id) => `/fi-ar/contabilizacion/${id}/conciliar-lotes-pos`,
      procesarAsiento: (id) => `/fi-ar/contabilizacion/${id}/procesar-asiento`,
      revisarAjusteAsientos: (id) => `/fi-ar/contabilizacion/${id}/revisar-ajuste-asientos`,
      auditarRecetas: (id) => `/fi-ar/contabilizacion/${id}/auditar-recetas`,
      solicitarDuplicadoReceta: (id) => `/fi-ar/contabilizacion/${id}/solicitar-duplicado-receta`,
      reintentarAuditoria: (id) => `/fi-ar/contabilizacion/${id}/reintentar-auditoria`,
      consolidarLote: (id) => `/fi-ar/contabilizacion/${id}/consolidar-lote`,
    },
    // FI-AR · Fase 03 — Auditoría Médica e Impugnación de Recetas (ZFMR_RECHAZO / ZFMR_IMPUGNACION)
    recetas: {
      list: '/fi-ar/recetas',
      registrar: '/fi-ar/recetas',
      getById: (id) => `/fi-ar/recetas/${id}`,
      validarTroquelesFirmas: (id) => `/fi-ar/recetas/${id}/validar-troqueles-firmas`,
      compararPreliquidacion: (id) => `/fi-ar/recetas/${id}/comparar-preliquidacion`,
      respuestaAseguradora: (id) => `/fi-ar/recetas/${id}/respuesta-aseguradora`,
      puedeContinuarFase04: '/fi-ar/recetas/puede-continuar-fase04',
    },
    // FI-AR · Fase 04 — Conciliación de Débitos y Ajustes Técnicos (RN-AR4-01)
    debitos: {
      list: '/fi-ar/debitos',
      calcular: '/fi-ar/debitos',
      getById: (id) => `/fi-ar/debitos/${id}`,
      evaluarJustificacion: (id) => `/fi-ar/debitos/${id}/evaluar-justificacion`,
      tramitarReclamo: (id) => `/fi-ar/debitos/${id}/tramitar-reclamo`,
      aplicarAjusteTecnico: (id) => `/fi-ar/debitos/${id}/aplicar-ajuste-tecnico`,
      puedeContinuarFase05: '/fi-ar/debitos/puede-continuar-fase05',
      ajusteTotal: '/fi-ar/debitos/ajuste-total',
    },
    // FI-AR · Fase 05 — Procesamiento de Cobros e Imputación Bancaria (RN-AR5-01)
    cobros: {
      list: '/fi-ar/cobros',
      interpretar: '/fi-ar/cobros',
      getById: (id) => `/fi-ar/cobros/${id}`,
      conciliarComisiones: (id) => `/fi-ar/cobros/${id}/conciliar-comisiones`,
      ajusteDiferencia: (id) => `/fi-ar/cobros/${id}/ajuste-diferencia`,
      registrarIngreso: (id) => `/fi-ar/cobros/${id}/registrar-ingreso`,
      puedeContinuarFase06: '/fi-ar/cobros/puede-continuar-fase06',
    },
    // FI-AR · Fase 06 — Compensación Final y Análisis de Margen Neto (RN-AR6-01)
    compensacionFinal: {
      list: '/fi-ar/compensacion-final',
      aplicar: '/fi-ar/compensacion-final',
      getById: (id) => `/fi-ar/compensacion-final/${id}`,
      generarReporte: (id) => `/fi-ar/compensacion-final/${id}/generar-reporte`,
      confirmarSaldo: (id) => `/fi-ar/compensacion-final/${id}/confirmar-saldo`,
      cerrarIngresos: (id) => `/fi-ar/compensacion-final/${id}/cerrar-ingresos`,
      cicloFinalizado: '/fi-ar/compensacion-final/ciclo-finalizado',
    },
  },
  logistica: {
    proveedores: {
      list: '/logistica/proveedores',
      create: '/logistica/proveedores',
      getById: (id) => `/logistica/proveedores/${id}`,
      update: (id) => `/logistica/proveedores/${id}`,
      delete: (id) => `/logistica/proveedores/${id}`,
    },
    convenios: {
      list: '/logistica/convenios',
      create: '/logistica/convenios',
      getById: (id) => `/logistica/convenios/${id}`,
    },
    solped: {
      mrp: '/logistica/solped/mrp',
      list: '/logistica/solped',
      create: '/logistica/solped',
      getById: (id) => `/logistica/solped/${id}`,
      aprobarFuente: (id) => `/logistica/solped/${id}/aprobar-fuente`,
      rechazar: (id) => `/logistica/solped/${id}/rechazar`,
    },
    // LOG.04 — Fase 03: tratamiento y emisión de la Orden de Compra (ME21N)
    ordenesCompra: {
      list: '/logistica/ordenes-compra',
      create: '/logistica/ordenes-compra',
      getById: (id) => `/logistica/ordenes-compra/${id}`,
      firmar: (id) => `/logistica/ordenes-compra/${id}/firmar`,
    },
    // LOG.05 — Fase 04: entrada de mercancía y registro (MIGO)
    entradasMercancia: {
      list: '/logistica/entradas-mercancia',
      registrar: '/logistica/entradas-mercancia',
      getById: (id) => `/logistica/entradas-mercancia/${id}`,
    },
    // LOG.06 — Fase 05: inspección y aseguramiento de calidad (QA11)
    inspeccionesCalidad: {
      list: '/logistica/inspecciones-calidad',
      aprobar: '/logistica/inspecciones-calidad/aprobar',
      rechazar: '/logistica/inspecciones-calidad/rechazar',
      getById: (id) => `/logistica/inspecciones-calidad/${id}`,
    },
    // LOG.07 — Fase 06: gestión de stocks y distribución capilar (ME27 / MB1B)
    sucursales: {
      list: '/logistica/sucursales',
      create: '/logistica/sucursales',
      getById: (id) => `/logistica/sucursales/${id}`,
    },
    ordenesTraslado: {
      list: '/logistica/ordenes-traslado',
      generar: '/logistica/ordenes-traslado',
      confirmarRecepcion: (id) => `/logistica/ordenes-traslado/${id}/confirmar-recepcion`,
      getById: (id) => `/logistica/ordenes-traslado/${id}`,
    },
    // LOG.07 — Fase 07: verificación de factura del proveedor (MIRO)
    facturasMiro: {
      list: '/logistica/facturas-miro',
      registrar: '/logistica/facturas-miro',
      getById: (id) => `/logistica/facturas-miro/${id}`,
    },
    // LOG.08 — Fase 08: conciliación de 3 vías (3-Way Match / MRBR)
    conciliaciones: {
      list: '/logistica/conciliaciones-tres-vias',
      ejecutar: '/logistica/conciliaciones-tres-vias',
      getById: (id) => `/logistica/conciliaciones-tres-vias/${id}`,
    },
    // LOG.09 — Fase 09: gestión y ejecución del pago (F110)
    pagos: {
      list: '/logistica/pagos',
      ejecutar: '/logistica/pagos',
      getById: (id) => `/logistica/pagos/${id}`,
    },
  },
};
