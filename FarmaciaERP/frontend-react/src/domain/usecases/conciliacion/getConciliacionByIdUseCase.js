import { ConciliacionTresVias } from '../../models/ConciliacionTresVias';

export const getConciliacionByIdUseCase = (conciliacionRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la conciliación es requerido');
    const raw = await conciliacionRepository.getById(id);
    return ConciliacionTresVias.fromApi(raw);
  },
});
