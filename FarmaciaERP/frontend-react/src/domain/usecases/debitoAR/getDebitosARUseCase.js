import { DebitoAR } from '../../models/DebitoAR';

export const getDebitosARUseCase = (debitoARRepository) => ({
  async execute(filters = {}) {
    const raw = await debitoARRepository.getAll(filters);
    return raw.map((item) => DebitoAR.fromApi(item));
  },
});
