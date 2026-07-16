import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const excepcionFacturacionRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.excepcionesFacturacion.list, {
      params: {
        ...(filters.conciliacionTresViasId && { conciliacionTresViasId: filters.conciliacionTresViasId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.excepcionesFacturacion.getById(id));
    return data;
  },

  async capturar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.excepcionesFacturacion.capturar, payload);
    return data;
  },

  async revisar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.excepcionesFacturacion.revisar(id));
    return data;
  },

  async clasificar(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.excepcionesFacturacion.clasificar(id), payload);
    return data;
  },
};
