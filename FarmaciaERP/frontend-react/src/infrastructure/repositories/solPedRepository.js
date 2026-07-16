import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const solPedRepository = {
  async calcularMRP(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.solped.mrp, payload);
    return data;
  },

  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.solped.list, {
      params: {
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.solped.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.solped.create, payload);
    return data;
  },

  async aprobarFuente(id, payload) {
    const { data } = await apiClient.put(ENDPOINTS.logistica.solped.aprobarFuente(id), payload);
    return data;
  },

  async rechazar(id, motivo) {
    const { data } = await apiClient.put(ENDPOINTS.logistica.solped.rechazar(id), { motivo });
    return data;
  },
};
