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
