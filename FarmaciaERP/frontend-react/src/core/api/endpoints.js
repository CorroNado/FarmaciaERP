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
  ventas: {
    list:    '/venta',
    create:  '/venta',
    getById: (id) => `/venta/${id}`,
    pagar:   (id) => `/venta/${id}/pagar`,
    anular:  (id) => `/venta/${id}/anular`,
  },
  clientes: {
    list:    '/cliente',
  },

  reports: {
    accessByUser: (id) => `/reports/access/${id}`,
  },
};