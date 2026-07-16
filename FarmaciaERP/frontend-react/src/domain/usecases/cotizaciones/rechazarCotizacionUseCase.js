import { Cotizacion } from '../../models/Cotizacion';

export const rechazarCotizacionUseCase = (cotizacionRepository) => ({
  async execute(id, motivo) {
    if (!id) throw new Error('El id de la cotización es requerido');
    if (!motivo) throw new Error('Selecciona el motivo de rechazo');
    const raw = await cotizacionRepository.rechazar(id, motivo);
    return Cotizacion.fromApi(raw);
  },
});
