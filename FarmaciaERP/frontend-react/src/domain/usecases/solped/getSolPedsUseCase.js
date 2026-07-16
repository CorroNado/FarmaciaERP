import { SolicitudPedido } from '../../models/SolicitudPedido';

export const getSolPedsUseCase = (solPedRepository) => ({
  async execute(filters = {}) {
    const raw = await solPedRepository.getAll(filters);
    return raw.map((item) => SolicitudPedido.fromApi(item));
  },
});
