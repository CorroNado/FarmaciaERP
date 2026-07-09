import apiClient from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const clienteService = {
  async getAll() {
    const { data } = await apiClient.get(ENDPOINTS.clientes.list);
    return data;
  }
};