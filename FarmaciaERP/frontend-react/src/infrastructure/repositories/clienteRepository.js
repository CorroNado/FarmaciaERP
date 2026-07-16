import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const clienteRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.clientes.list, {
      params: {
        ...(filters.dni && { dni: filters.dni }),
        ...(filters.nombres && { nombres: filters.nombres }),
        ...(filters.tipoSeguro && { tipoSeguro: filters.tipoSeguro }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getByDni(dni) {
    const { data } = await apiClient.get(ENDPOINTS.clientes.list, { params: { dni } });
    const list = Array.isArray(data) ? data : (data.data ?? data.items ?? []);
    return list[0] ?? null;
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.clientes.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.clientes.create, payload);
    return data;
  },
};
