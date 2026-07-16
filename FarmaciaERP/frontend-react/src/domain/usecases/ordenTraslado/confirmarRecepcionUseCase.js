import { OrdenTraslado } from '../../models/OrdenTraslado';

export const confirmarRecepcionUseCase = (ordenTrasladoRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la Orden de Traslado es requerido');
    const raw = await ordenTrasladoRepository.confirmarRecepcion(id);
    return OrdenTraslado.fromApi(raw);
  },
});
