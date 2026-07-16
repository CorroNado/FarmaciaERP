import { Venta } from '../../models/Venta';

export const crearVentaUseCase = (ventaRepository) => ({
  async execute({ clienteId, metodoPago, tipoComprobante, cart }) {
    if (!clienteId) {
      throw new Error('Selecciona un cliente para continuar');
    }
    if (!cart || cart.length === 0) {
      throw new Error('El carrito está vacío');
    }
    if (!metodoPago) {
      throw new Error('Selecciona un método de pago');
    }

    const payload = Venta.toApiCrear({ clienteId, metodoPago, tipoComprobante, cart });
    const raw = await ventaRepository.create(payload);
    return Venta.fromApi(raw);
  },
});
