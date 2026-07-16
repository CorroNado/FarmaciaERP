import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const recetaMedicaARRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.recetas.list, {
      params: {
        ...(filters.contabilizacionARId && { contabilizacionARId: filters.contabilizacionARId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.recetas.getById(id));
    return data;
  },

  async registrar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.recetas.registrar, payload);
    return data;
  },

  async validarTroquelesFirmas(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.recetas.validarTroquelesFirmas(id), payload);
    return data;
  },

  async compararPreliquidacion(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.recetas.compararPreliquidacion(id), payload);
    return data;
  },

  async registrarRespuestaAseguradora(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.recetas.respuestaAseguradora(id), payload);
    return data;
  },

  async puedeContinuarFase04(contabilizacionARId) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.recetas.puedeContinuarFase04, {
      params: { contabilizacionARId },
    });
    return Boolean(data?.puedeContinuarFase04);
  },
};
