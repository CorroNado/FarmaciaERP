import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const compensacionARRepository = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.compensacionFinal.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  // El backend devuelve una única CompensacionAR (o 404) para un lote de la Fase 02
  async getByContabilizacionAR(contabilizacionARId) {
    try {
      const { data } = await apiClient.get(ENDPOINTS.fiAr.compensacionFinal.list, {
        params: { contabilizacionARId },
      });
      return data ?? null;
    } catch (err) {
      if (err.response?.status === 404) return null;
      throw err;
    }
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.compensacionFinal.getById(id));
    return data;
  },

  async aplicar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.compensacionFinal.aplicar, payload);
    return data;
  },

  async generarReporte(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.compensacionFinal.generarReporte(id));
    return data;
  },

  async confirmarSaldo(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.compensacionFinal.confirmarSaldo(id));
    return data;
  },

  async cerrarIngresos(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.compensacionFinal.cerrarIngresos(id));
    return data;
  },

  async cicloFinalizado(contabilizacionARId) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.compensacionFinal.cicloFinalizado, {
      params: { contabilizacionARId },
    });
    return Boolean(data?.cicloFinalizado);
  },
};
