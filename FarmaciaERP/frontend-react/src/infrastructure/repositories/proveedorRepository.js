import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const proveedorRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.proveedores.list, {
      params: {
        ...(filters.razonSocial && { razonSocial: filters.razonSocial }),
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.proveedores.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.proveedores.create, payload);
    return data;
  },

  async update(id, payload) {
    const { data } = await apiClient.put(ENDPOINTS.logistica.proveedores.update(id), payload);
    return data;
  },

  async delete(id) {
    await apiClient.delete(ENDPOINTS.logistica.proveedores.delete(id));
  },
};
