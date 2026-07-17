import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

const DEFAULT_USUARIO = 'Sistema';

export const empleadoRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.empleados.list, {
      params: {
        ...(filters.texto && { texto: filters.texto }),
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.empleados.getById(id));
    return data;
  },

  async create(payload, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.empleados.create, payload, {
      params: { usuario },
    });
    return data;
  },

  async update(id, payload, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.put(ENDPOINTS.rrhh.empleados.update(id), payload, {
      params: { usuario },
    });
    return data;
  },

  async delete(id, usuario = DEFAULT_USUARIO) {
    await apiClient.delete(ENDPOINTS.rrhh.empleados.delete(id), { params: { usuario } });
  },

  async reactivar(id, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.empleados.reactivar(id), { usuario });
    return data;
  },

  async bajaSinTurnos(id, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.empleados.bajaSinTurnos(id), { usuario });
    return data;
  },

  async bajaInmediata(id, { motivo, turnoInfo }, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.empleados.bajaInmediata(id), {
      usuario, motivo, turnoInfo,
    });
    return data;
  },

  async bajaProgramada(id, { fechaEfectiva, observacion, turnoInfo }, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.empleados.bajaProgramada(id), {
      usuario, fechaEfectiva, observacion, turnoInfo,
    });
    return data;
  },

  async auditoria(codigo) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.empleados.auditoria, {
      params: { ...(codigo && { codigo }) },
    });
    return data;
  },
};
