import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const ajusteContableRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.ajustesContables.list, {
      params: {
        ...(filters.disputaComercialId && { disputaComercialId: filters.disputaComercialId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.ajustesContables.getById(id));
    return data;
  },

  async iniciar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.iniciar, payload);
    return data;
  },

  async recepcionNotaCredito(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.recepcionNotaCredito(id), payload);
    return data;
  },

  async gestionarReclamo(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.gestionarReclamo(id));
    return data;
  },

  async evaluarEnvioNotaCredito(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.evaluarEnvioNotaCredito(id));
    return data;
  },

  async registrarNotaCredito(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.registrarNotaCredito(id));
    return data;
  },

  async asientoRegularizacion(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.asientoRegularizacion(id));
    return data;
  },

  async desbloquearPartida(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.ajustesContables.desbloquearPartida(id));
    return data;
  },
};
