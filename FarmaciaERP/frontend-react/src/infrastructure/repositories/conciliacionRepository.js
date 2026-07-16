import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const conciliacionRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.conciliaciones.list, {
      params: {
        ...(filters.ordenCompraId && { ordenCompraId: filters.ordenCompraId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.conciliaciones.getById(id));
    return data;
  },

  async ejecutar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.conciliaciones.ejecutar, payload);
    return data;
  },
};
