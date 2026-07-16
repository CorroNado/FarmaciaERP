import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const pagoRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.pagos.list, {
      params: {
        ...(filters.facturaMIROId && { facturaMIROId: filters.facturaMIROId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.pagos.getById(id));
    return data;
  },

  async ejecutar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.pagos.ejecutar, payload);
    return data;
  },
};
