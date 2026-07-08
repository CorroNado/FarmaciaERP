import { AccessReport } from '@/domain/models/AccessReport';

export const getAccessReportUseCase = (reportRepository) => ({
  async execute(userId) {
    if (!userId) throw new Error('El id del usuario es requerido');

    const raw = await reportRepository.getAccessByUser(userId);
    return AccessReport.fromApi(raw);
  },
});