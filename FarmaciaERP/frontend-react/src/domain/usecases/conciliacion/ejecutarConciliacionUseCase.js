import { ConciliacionTresVias } from '../../models/ConciliacionTresVias';

export const ejecutarConciliacionUseCase = (conciliacionRepository) => ({
  async execute({ ordenCompraId }) {
    if (!ordenCompraId) {
      throw new Error('Debe seleccionar una Orden de Compra con factura (MIRO) registrada');
    }

    const payload = ConciliacionTresVias.toApiEjecutar({ ordenCompraId });
    const raw = await conciliacionRepository.ejecutar(payload);
    return ConciliacionTresVias.fromApi(raw);
  },
});
