import apiClient     from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const ventaRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.ventas.list, {
      params: {
        ...(filters.clienteId && { clienteId: filters.clienteId }),
        ...(filters.estado    && { estado:    filters.estado    }),
        ...(filters.fecha     && { fecha:     filters.fecha     }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.ventas.getById(id));
    return data;
  },

  /*async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.ventas.create, payload);
    return data;
  },*/

  async pagar(id) {
    const { data } = await apiClient.put(ENDPOINTS.ventas.pagar(id));
    return data;
  },

  async anular(id) {
    const { data } = await apiClient.put(ENDPOINTS.ventas.anular(id));
    return data;
  },
  async create(payload) {
  const token = localStorage.getItem('access_token');
  console.log('Token al crear venta:', token);
  console.log('Payload:', JSON.stringify(payload, null, 2));
  const { data } = await apiClient.post(ENDPOINTS.ventas.create, payload);
  return data;
},
};
