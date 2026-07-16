import { Cotizacion } from '../../models/Cotizacion';
import { Venta } from '../../models/Venta';

// SD.02.03 -> SD.03.01: el cliente acepta la cotización y esta se convierte
// en un pedido de venta. El backend devuelve tanto la cotización actualizada
// como la venta recién generada.
export const aceptarCotizacionUseCase = (cotizacionRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la cotización es requerido');
    const raw = await cotizacionRepository.aceptar(id);
    return {
      cotizacion: Cotizacion.fromApi(raw.cotizacion),
      venta: Venta.fromApi(raw.venta),
    };
  },
});
