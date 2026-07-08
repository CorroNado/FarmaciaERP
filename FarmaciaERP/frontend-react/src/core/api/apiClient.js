import axios from 'axios';
import { tokenStorage } from '@/core/utils/tokenStorage';

const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config) => {
    const token = tokenStorage.get();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    if (status === 401) {
      tokenStorage.remove();
      window.location.href = '/login';
    }
    if (status === 403) console.warn('Sin permisos para esta acción');
    if (status >= 500) console.error('Error del servidor:', error.response?.data?.message);
    return Promise.reject(error);
  }
);

export default apiClient;