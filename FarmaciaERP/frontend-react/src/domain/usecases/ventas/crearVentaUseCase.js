import { Venta } from '../../models/Venta';

export const crearVentaUseCase = (ventaRepository) => ({
  async execute(formData) {
    if (!formData.detalles || formData.detalles.length === 0) {
      throw new Error('Debe agregar al menos un producto');
    }
    const payload = Venta.toApi(formData);
    console.log('Payload:', JSON.stringify(payload, null, 2));
    const raw = await ventaRepository.create(payload);
    return Venta.fromApi(raw);
  },
});