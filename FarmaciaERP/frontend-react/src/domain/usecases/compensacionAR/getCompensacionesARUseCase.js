import { CompensacionAR } from '../../models/CompensacionAR';

export const getCompensacionesARUseCase = (compensacionARRepository) => ({
  async execute() {
    const raw = await compensacionARRepository.getAll();
    return raw.map((item) => CompensacionAR.fromApi(item));
  },
});
