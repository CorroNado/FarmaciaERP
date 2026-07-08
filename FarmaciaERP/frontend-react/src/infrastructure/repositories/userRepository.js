import apiClient     from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const userRepository = {
  async getAll(filters = {}) {
    const { data } = await apiClient.get(ENDPOINTS.users.list, {
      params: {
        ...(filters.nombre && { nombre: filters.nombre }),
        ...(filters.estado && { estado: filters.estado }),
        ...(filters.rol    && { rol:    filters.rol    }),
      },
    });
    return Array.isArray(data) ? data : (data.data ?? data.items ?? []);
  },

  async getById(id) {
    const { data } = await apiClient.get(ENDPOINTS.users.getById(id));
    return data;
  },

  async create(payload) {
  console.log('Payload:', JSON.stringify(payload, null, 2));
  const { data } = await apiClient.post(ENDPOINTS.users.create, payload);
  return data;
},

async update(id, payload) {
  console.log('PUT URL:', `/usuario/${id}`);
  console.log('Payload:', JSON.stringify(payload, null, 2));
  const { data } = await apiClient.put(ENDPOINTS.users.update(id), payload);
  return data;
},

  async delete(id) {
    await apiClient.delete(ENDPOINTS.users.delete(id));
  },
  
};