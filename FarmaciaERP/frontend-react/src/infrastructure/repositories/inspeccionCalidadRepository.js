import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const inspeccionCalidadRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.inspeccionesCalidad.list, {
      params: {
        ...(filters.entradaMercanciaId && { entradaMercanciaId: filters.entradaMercanciaId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.inspeccionesCalidad.getById(id));
    return data;
  },

  async aprobar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.inspeccionesCalidad.aprobar, payload);
    return data;
  },

  async rechazar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.inspeccionesCalidad.rechazar, payload);
    return data;
  },
};
