import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

const DEFAULT_USUARIO = 'Sistema';

export const asistenciaRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.asistencias.list, {
      params: {
        ...(filters.fecha && { fecha: filters.fecha }),
        ...(filters.empleadoId && { empleadoId: filters.empleadoId }),
        ...(filters.mes && { mes: filters.mes }),
        ...(filters.anio && { anio: filters.anio }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.asistencias.getById(id));
    return data;
  },

  async programar(payload) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.asistencias.programar, payload);
    return data;
  },

  async marcarEntrada(id, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.asistencias.entrada(id), null, {
      params: { usuario },
    });
    return data;
  },

  async marcarSalida(id, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.asistencias.salida(id), null, {
      params: { usuario },
    });
    return data;
  },

  async justificar(id, { motivo }, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.post(ENDPOINTS.rrhh.asistencias.justificar(id), {
      usuario, motivo,
    });
    return data;
  },

  async editar(id, { horaEntrada, estado, motivoAuditoria }, usuario = DEFAULT_USUARIO) {
    const { data } = await apiClient.put(ENDPOINTS.rrhh.asistencias.update(id), {
      usuario, horaEntrada, estado, motivoAuditoria,
    });
    return data;
  },

  async eliminar(id, { motivoAuditoria }, usuario = DEFAULT_USUARIO) {
    await apiClient.delete(ENDPOINTS.rrhh.asistencias.delete(id), {
      data: { usuario, motivoAuditoria },
    });
  },

  async auditoria(codigoEmpleado) {
    const { data } = await apiClient.get(ENDPOINTS.rrhh.asistencias.auditoria, {
      params: { ...(codigoEmpleado && { codigoEmpleado }) },
    });
    return data;
  },
};
