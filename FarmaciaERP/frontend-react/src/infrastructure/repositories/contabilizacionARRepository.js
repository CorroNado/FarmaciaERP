import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const contabilizacionARRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.contabilizacion.list, {
      params: {
        ...(filters.cierreCajaId && { cierreCajaId: filters.cierreCajaId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAr.contabilizacion.getById(id));
    return data;
  },

  async getByCierreCaja(cierreCajaId) {
    try {
      const { data } = await apiClient.get(ENDPOINTS.fiAr.contabilizacion.list, {
        params: { cierreCajaId },
      });
      return Array.isArray(data) ? null : data;
    } catch (err) {
      if (err.response?.status === 404) return null;
      throw err;
    }
  },

  async iniciar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.iniciar, payload);
    return data;
  },

  async conciliarLotesPOS(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.conciliarLotesPOS(id));
    return data;
  },

  async procesarAsiento(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.procesarAsiento(id));
    return data;
  },

  async revisarAjusteAsientos(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.revisarAjusteAsientos(id));
    return data;
  },

  async auditarRecetas(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.auditarRecetas(id), payload);
    return data;
  },

  async solicitarDuplicadoReceta(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.solicitarDuplicadoReceta(id));
    return data;
  },

  async reintentarAuditoria(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.reintentarAuditoria(id));
    return data;
  },

  async consolidarLote(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAr.contabilizacion.consolidarLote(id));
    return data;
  },
};
