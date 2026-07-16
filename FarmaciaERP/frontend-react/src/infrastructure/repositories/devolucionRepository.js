import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const devolucionRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.devoluciones.list, {
      params: {
        ...(filters.ventaId && { ventaId: filters.ventaId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.devoluciones.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.devoluciones.create, payload);
    return data;
  },
};
