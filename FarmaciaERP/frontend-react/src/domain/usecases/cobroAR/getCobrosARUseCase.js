import { CobroAR } from '../../models/CobroAR';

export const getCobrosARUseCase = (cobroARRepository) => ({
  async execute() {
    const raw = await cobroARRepository.getAll();
    return raw.map((item) => CobroAR.fromApi(item));
  },
});
