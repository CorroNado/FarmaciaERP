import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const planillaRepository = {
  async calcular({ mes, anio }) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.planillas.calcular, { mes: Number(mes), anio: Number(anio) });
    return data;
  },

  async guardar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.planillas.guardar, payload);
    return data;
  },

  async historial() {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.planillas.list);
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.planillas.getById(id));
    return data;
  },

  async getByMesAnio(mes, anio) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.planillas.buscar, { params: { mes, anio } });
    return data;
  },

  async eliminar(id) {
    await apiClient.delete(ENDPOINTS.rrhh.planillas.delete(id));
  },
};
