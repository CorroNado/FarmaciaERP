import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const cotizacionRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.cotizaciones.list, {
      params: {
        ...(filters.clienteId && { clienteId: filters.clienteId }),
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.cotizaciones.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.cotizaciones.create, payload);
    return data;
  },

  async aceptar(id) {
    const { data } = await apiClient.put(ENDPOINTS.cotizaciones.aceptar(id));
    return data;
  },

  async rechazar(id, motivo) {
    const { data } = await apiClient.put(ENDPOINTS.cotizaciones.rechazar(id), { motivo });
    return data;
  },
};
