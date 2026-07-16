import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const propuestaPagoRepository = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.propuestasPago.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.fiAp.propuestasPago.getById(id));
    return data;
  },

  async iniciar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.iniciar, payload);
    return data;
  },

  async introducirParametros(id, payload) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.parametros(id), payload);
    return data;
  },

  async ejecutarPropuesta(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.ejecutarPropuesta(id));
    return data;
  },

  async revisarExcepciones(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.revisarExcepciones(id));
    return data;
  },

  async ajustarReejecutar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.ajustarReejecutar(id));
    return data;
  },

  async aprobar(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.aprobar(id));
    return data;
  },

  async ejecutarPago(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.ejecutarPago(id));
    return data;
  },

  async generarArchivos(id) {
    const { data } = await apiClient.post(ENDPOINTS.fiAp.propuestasPago.generarArchivos(id));
    return data;
  },
};
