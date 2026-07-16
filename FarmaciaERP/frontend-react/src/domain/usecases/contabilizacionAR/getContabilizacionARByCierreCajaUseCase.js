import { ContabilizacionAR } from '../../models/ContabilizacionAR';

export const getContabilizacionARByCierreCajaUseCase = (contabilizacionARRepository) => ({
  async execute(cierreCajaId) {
    if (!cierreCajaId) throw new Error('El id del cierre de caja es requerido');
    const raw = await contabilizacionARRepository.getByCierreCaja(cierreCajaId);
    return raw ? ContabilizacionAR.fromApi(raw) : null;
  },
});
