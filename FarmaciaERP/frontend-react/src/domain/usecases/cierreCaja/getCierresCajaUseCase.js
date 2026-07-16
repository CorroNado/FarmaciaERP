import { CierreCaja } from '../../models/CierreCaja';

export const getCierresCajaUseCase = (cierreCajaRepository) => ({
  async execute(filters = {}) {
    const raw = await cierreCajaRepository.getAll(filters);
    return raw.map((item) => CierreCaja.fromApi(item));
  },
});
