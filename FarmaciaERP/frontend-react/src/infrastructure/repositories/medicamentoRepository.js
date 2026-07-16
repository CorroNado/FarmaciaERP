import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const medicamentoRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.medicamentos.list, {
      params: {
        ...(filters.nombre && { nombre: filters.nombre }),
        ...(filters.categoria && { categoria: filters.categoria }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.medicamentos.getById(id));
    return data;
  },
};
