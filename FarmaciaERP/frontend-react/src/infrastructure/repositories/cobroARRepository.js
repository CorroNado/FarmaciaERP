import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const cobroARRepository = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.cobros.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  // El backend devuelve un único CobroAR (o 404) para un lote de la Fase 02
  async getByContabilizacionAR(contabilizacionARId) {
    try {
      const { data } = await apiClient.get(ENDPOINTS.fiAr.cobros.list, {
        params: { contabilizacionARId },
      });
      return data ?? null;
    } catch (err) {
      if (err.response?.status === 404) return null;
      throw err;
    }
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.cobros.getById(id));
    return data;
  },

  async interpretar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cobros.interpretar, payload);
    return data;
  },

  async conciliarComisiones(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cobros.conciliarComisiones(id), payload);
    return data;
  },

  async ajusteDiferencia(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cobros.ajusteDiferencia(id));
    return data;
  },

  async registrarIngreso(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cobros.registrarIngreso(id));
    return data;
  },

  async puedeContinuarFase06(contabilizacionARId) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.cobros.puedeContinuarFase06, {
      params: { contabilizacionARId },
    });
    return Boolean(data?.puedeContinuarFase06);
  },
};
