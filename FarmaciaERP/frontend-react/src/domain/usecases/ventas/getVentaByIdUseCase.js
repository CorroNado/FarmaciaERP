import { Venta } from '../../models/Venta';

export const getVentaByIdUseCase = (ventaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id es requerido');
    const raw = await ventaRepository.getById(id);
    return Venta.fromApi(raw);
  },
});