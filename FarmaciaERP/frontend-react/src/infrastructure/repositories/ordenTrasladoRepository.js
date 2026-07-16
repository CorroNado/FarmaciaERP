import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const ordenTrasladoRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.ordenesTraslado.list, {
      params: {
        ...(filters.inspeccionCalidadId && { inspeccionCalidadId: filters.inspeccionCalidadId }),
        ...(filters.sucursalId && { sucursalId: filters.sucursalId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.ordenesTraslado.getById(id));
    return data;
  },

  async generar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.ordenesTraslado.generar, payload);
    return data;
  },

  async confirmarRecepcion(id) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.ordenesTraslado.confirmarRecepcion(id));
    return data;
  },
};
