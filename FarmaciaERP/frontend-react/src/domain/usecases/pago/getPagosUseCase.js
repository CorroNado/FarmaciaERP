import { Pago } from '../../models/Pago';

export const getPagosUseCase = (pagoRepository) => ({
  async execute(filters = {}) {
    const raw = await pagoRepository.getAll(filters);
    return raw.map((item) => Pago.fromApi(item));
  },
});
