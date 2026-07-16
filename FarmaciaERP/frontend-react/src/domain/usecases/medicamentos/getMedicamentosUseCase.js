import { Medicamento } from '../../models/Medicamento';

export const getMedicamentosUseCase = (medicamentoRepository) => ({
  async execute(filters = {}) {
    const raw = await medicamentoRepository.getAll(filters);
    return raw.map((item) => Medicamento.fromApi(item));
  },
});
