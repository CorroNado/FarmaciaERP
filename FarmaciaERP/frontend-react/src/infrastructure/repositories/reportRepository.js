import apiClient     from '@/core/api/apiClient';
import { ENDPOINTS } from '@/core/api/endpoints';

export const reportRepository = {
  async getAccessByUser(userId) {
    const { data } = await apiClient.get(
      ENDPOINTS.reports.accessByUser(userId)
    );
    return data;
  },
};