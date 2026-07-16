import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const migoRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.entradasMercancia.list, {
      params: {
        ...(filters.ordenCompraId && { ordenCompraId: filters.ordenCompraId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.entradasMercancia.getById(id));
    return data;
  },

  async registrar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.entradasMercancia.registrar, payload);
    return data;
  },
};
