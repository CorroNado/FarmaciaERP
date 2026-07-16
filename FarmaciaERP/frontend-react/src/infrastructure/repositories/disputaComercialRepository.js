import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const disputaComercialRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.disputasComerciales.list, {
      params: {
        ...(filters.excepcionFacturacionId && { excepcionFacturacionId: filters.excepcionFacturacionId }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.disputasComerciales.getById(id));
    return data;
  },

  async abrir(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.abrir, payload);
    return data;
  },

  async cotejar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.cotejar(id));
    return data;
  },

  async cuantificar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.cuantificar(id));
    return data;
  },

  async validarDesviacion(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.validarDesviacion(id));
    return data;
  },

  async abrirNegociacion(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.abrirNegociacion(id));
    return data;
  },

  async contraoferta(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.contraoferta(id), payload);
    return data;
  },

  async aceptarAbsorcion(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.aceptarAbsorcion(id));
    return data;
  },

  async reabrirNegociacion(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.reabrirNegociacion(id));
    return data;
  },

  async resolverWorkflow(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.disputasComerciales.resolverWorkflow(id));
    return data;
  },
};
