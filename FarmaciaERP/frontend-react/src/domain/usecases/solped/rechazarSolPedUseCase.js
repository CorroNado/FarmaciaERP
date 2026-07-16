import { SolicitudPedido } from '../../models/SolicitudPedido';

export const rechazarSolPedUseCase = (solPedRepository) => ({
  async execute(id, motivo) {
    if (!id) throw new Error('El id de la SolPed es requerido');
    if (!motivo || !motivo.trim()) throw new Error('El motivo de rechazo es obligatorio');

    const raw = await solPedRepository.rechazar(id, motivo);
    return SolicitudPedido.fromApi(raw);
  },
});
