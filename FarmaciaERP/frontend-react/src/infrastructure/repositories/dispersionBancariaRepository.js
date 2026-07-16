import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const dispersionBancariaRepository = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.dispersionesBancarias.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.dispersionesBancarias.getById(id));
    return data;
  },

  async iniciar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.iniciar, payload);
    return data;
  },

  async compilar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.compilar(id));
    return data;
  },

  async validar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.validar(id));
    return data;
  },

  async corregirReenviar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.corregirReenviar(id));
    return data;
  },

  async generarArchivo(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.generarArchivo(id));
    return data;
  },

  async firmar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.firmar(id));
    return data;
  },

  async transferir(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.transferir(id));
    return data;
  },

  async importarExtracto(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.importarExtracto(id));
    return data;
  },

  async conciliar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.dispersionesBancarias.conciliar(id));
    return data;
  },
};
