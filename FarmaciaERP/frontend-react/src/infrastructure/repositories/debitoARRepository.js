import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const debitoARRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.debitos.list, {
      params: {
        ...(filters.contabilizacionARId && { contabilizacionARId: filters.contabilizacionARId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.debitos.getById(id));
    return data;
  },

  async calcular(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.debitos.calcular, payload);
    return data;
  },

  async evaluarJustificacion(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.debitos.evaluarJustificacion(id), payload);
    return data;
  },

  async tramitarReclamo(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.debitos.tramitarReclamo(id));
    return data;
  },

  async aplicarAjusteTecnico(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.debitos.aplicarAjusteTecnico(id));
    return data;
  },

  async puedeContinuarFase05(contabilizacionARId) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.debitos.puedeContinuarFase05, {
      params: { contabilizacionARId },
    });
    return Boolean(data?.puedeContinuarFase05);
  },

  async ajusteTotal(contabilizacionARId) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.debitos.ajusteTotal, {
      params: { contabilizacionARId },
    });
    return Number(data?.ajusteTotal ?? 0);
  },
};
