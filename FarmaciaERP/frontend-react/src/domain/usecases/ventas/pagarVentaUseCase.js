import { Venta } from '../../models/Venta';

export const pagarVentaUseCase = (ventaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id es requerido');
    const raw = await ventaRepository.pagar(id);
    return Venta.fromApi(raw);
  },
});