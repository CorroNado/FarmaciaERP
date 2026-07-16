import { CierreCaja } from '../../models/CierreCaja';

export const getCierreCajaByIdUseCase = (cierreCajaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del cierre de caja es requerido');
    const raw = await cierreCajaRepository.getById(id);
    return CierreCaja.fromApi(raw);
  },
});
