import { Venta } from '../../models/Venta';

export const anularVentaUseCase = (ventaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id es requerido');
    const raw = await ventaRepository.anular(id);
    return Venta.fromApi(raw);
  },
});