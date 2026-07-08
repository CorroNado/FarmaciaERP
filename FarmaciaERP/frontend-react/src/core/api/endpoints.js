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
};