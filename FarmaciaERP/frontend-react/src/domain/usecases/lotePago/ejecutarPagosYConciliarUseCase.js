import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.5 - Ejecutar Pagos y Conciliar en SAP FI-AP (RN-AP4-08: habilita la Fase 05)
export const ejecutarPagosYConciliarUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.ejecutarConciliar(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
