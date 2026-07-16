import { ConciliacionTresVias } from '../../models/ConciliacionTresVias';

export const getConciliacionesUseCase = (conciliacionRepository) => ({
  async execute(filters = {}) {
    const raw = await conciliacionRepository.getAll(filters);
    return raw.map((item) => ConciliacionTresVias.fromApi(item));
  },
});
