import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const lotePagoRepository = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.lotesPago.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.lotesPago.getById(id));
    return data;
  },

  async iniciar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.iniciar, payload);
    return data;
  },

  async priorizar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.priorizar(id));
    return data;
  },

  async negociarDescuento(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.negociarDescuento(id), payload);
    return data;
  },

  async preparar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.preparar(id));
    return data;
  },

  async verificarFondos(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.verificarFondos(id));
    return data;
  },

  async someterComite(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.someterComite(id));
    return data;
  },

  async corregir(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.corregir(id));
    return data;
  },

  async ejecutarConciliar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.lotesPago.ejecutarConciliar(id));
    return data;
  },
};
