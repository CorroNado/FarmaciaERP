import { EntradaMercancia } from '../../models/EntradaMercancia';

export const registrarMIGOUseCase = (migoRepository) => ({
  async execute({ ordenCompraId, lote, fechaVencimiento, temperaturaArribo, cantidadRecibida, confirmarExcepcion }) {
    if (!ordenCompraId) {
      throw new Error('Debe seleccionar una Orden de Compra firmada');
    }
    // RN-F04-013: lote y fecha de vencimiento son obligatorios (validado también en backend)
    if (!lote || !lote.trim()) {
      throw new Error('RN-F04-013: el N° de lote es obligatorio');
    }
    if (!fechaVencimiento || !fechaVencimiento.trim()) {
      throw new Error('RN-F04-013: la fecha de vencimiento es obligatoria');
    }
    if (cantidadRecibida === '' || cantidadRecibida === null || Number.isNaN(Number(cantidadRecibida))) {
      throw new Error('La cantidad recibida es obligatoria');
    }

    const payload = EntradaMercancia.toApiRegistrar({
      ordenCompraId, lote, fechaVencimiento, temperaturaArribo, cantidadRecibida, confirmarExcepcion,
    });
    const raw = await migoRepository.registrar(payload);
    return EntradaMercancia.fromApi(raw);
  },
});
