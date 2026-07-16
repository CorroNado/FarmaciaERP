import { LotePagoTesoreria } from '../../models/LotePagoTesoreria';

export const getLotesPagoUseCase = (lotePagoRepository) => ({
  async execute() {
    const raw = await lotePagoRepository.getAll();
    return raw.map((item) => LotePagoTesoreria.fromApi(item));
  },
});
