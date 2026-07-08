import apiClient     from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const authService = {
  async login(credentials) {
    const { data } = await apiClient.post(ENDPOINTS.auth.login, {
      email:    credentials.email,
      password: credentials.password,
    });
    return data;
  },

  async getMe() {
    const { data } = await apiClient.get(ENDPOINTS.users.list);
    return data;
  },
};