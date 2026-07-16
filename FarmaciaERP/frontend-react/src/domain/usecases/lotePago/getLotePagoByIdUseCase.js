import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

export const getLotePagoByIdUseCase = (lotePagoRepository) => ({
  async execute(id) {
    const raw = await lotePagoRepository.getById(id);
    return LotePagoTesoreria.fromApi(raw);
  },
});
