import { PropuestaPagoAutomatica } from '../../models/PropuestaPagoAutomatica';

export const getPropuestasPagoUseCase = (propuestaPagoRepository) => ({
  async execute() {
    const raw = await propuestaPagoRepository.getAll();
    return raw.map((item) => PropuestaPagoAutomatica.fromApi(item));
  },
});
