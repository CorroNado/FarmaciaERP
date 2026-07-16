import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const convenioRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.convenios.list, {
      params: {
        ...(filters.proveedorId && { proveedorId: filters.proveedorId }),
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.convenios.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.convenios.create, payload);
    return data;
  },
};
