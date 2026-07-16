import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

export const getPropuestaPagoByIdUseCase = (propuestaPagoRepository) => ({
  async execute(id) {
    const raw = await propuestaPagoRepository.getById(id);
    return PropuestaPagoAutomatica.fromApi(raw);
  },
});
