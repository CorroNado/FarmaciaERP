import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const getRecetasMedicasARUseCase = (recetaMedicaARRepository) => ({
  async execute(filters = {}) {
    const raw = await recetaMedicaARRepository.getAll(filters);
    return raw.map((item) => RecetaMedicaAR.fromApi(item));
  },
});
