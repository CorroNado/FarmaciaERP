import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const facturaMiroRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.facturasMiro.list, {
      params: {
        ...(filters.ordenCompraId && { ordenCompraId: filters.ordenCompraId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.facturasMiro.getById(id));
    return data;
  },

  async registrar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.facturasMiro.registrar, payload);
    return data;
  },
};
