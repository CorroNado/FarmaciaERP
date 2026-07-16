import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const sucursalRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.sucursales.list, {
      params: {
        ...(filters.soloActivas && { soloActivas: true }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.sucursales.getById(id));
    return data;
  },

  async crear(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.sucursales.create, payload);
    return data;
  },
};
