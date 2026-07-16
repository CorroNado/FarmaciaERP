import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const ordenCompraRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.ordenesCompra.list, {
      params: {
        ...(filters.solPedId && { solPedId: filters.solPedId }),
        ...(filters.estado && { estado: filters.estado }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.logistica.ordenesCompra.getById(id));
    return data;
  },

  async create(payload) {
    const { data } = await apiClient.post(ENDPOINTS.logistica.ordenesCompra.create, payload);
    return data;
  },

  async firmar(id, fechaEntregaLimite) {
    const { data } = await apiClient.put(ENDPOINTS.logistica.ordenesCompra.firmar(id), { fechaEntregaLimite });
    return data;
  },
};
