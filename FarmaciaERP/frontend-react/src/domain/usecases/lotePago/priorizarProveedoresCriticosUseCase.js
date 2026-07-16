import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

// 4.1 - Priorizar Proveedores Críticos de Medicamentos (Analista de Cuentas por Pagar)
export const priorizarProveedoresCriticosUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.priorizar(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
