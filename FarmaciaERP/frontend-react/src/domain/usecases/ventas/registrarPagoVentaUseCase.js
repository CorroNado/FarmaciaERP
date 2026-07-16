import { Venta } from '../../models/Venta';

export const registrarPagoVentaUseCase = (ventaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la venta es requerido');
    const raw = await ventaRepository.pagar(id);
    return Venta.fromApi(raw);
  },
});
