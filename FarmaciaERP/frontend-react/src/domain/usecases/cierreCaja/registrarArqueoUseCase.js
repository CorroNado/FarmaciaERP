import { CierreCaja } from '../../models/CierreCaja';

export const registrarArqueoUseCase = (cierreCajaRepository) => ({
  async execute(id, { montoContado }) {
    if (!id) throw new Error('El id del cierre de caja es requerido');
    if (montoContado === '' || montoContado === null || montoContado === undefined || isNaN(montoContado)) {
      throw new Error('Ingresa el monto contado en caja');
    }

    const payload = CierreCaja.toApiArqueo({ montoContado });
    const raw = await cierreCajaRepository.registrarArqueo(id, payload);
    return CierreCaja.fromApi(raw);
  },
});
