import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const cierreCajaRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.cierreCaja.list, {
      params: {
        ...(filters.sucursalId && { sucursalId: filters.sucursalId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.cierreCaja.getById(id));
    return data;
  },

  async abrir(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cierreCaja.abrir, payload);
    return data;
  },

  async registrarArqueo(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cierreCaja.arqueo(id), payload);
    return data;
  },

  async registrarJustificacion(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cierreCaja.justificacion(id), payload);
    return data;
  },

  async enviarFisicosRecetas(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cierreCaja.enviarFisicosRecetas(id));
    return data;
  },

  async clasificarCopagoCobertura(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.cierreCaja.clasificarCopagoCobertura(id));
    return data;
  },
};
