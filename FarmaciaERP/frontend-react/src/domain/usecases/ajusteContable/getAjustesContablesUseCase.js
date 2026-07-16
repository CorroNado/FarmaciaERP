import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

export const getAjustesContablesUseCase = (ajusteContableRepository) => ({
  async execute(filters = {}) {
    const raw = await ajusteContableRepository.getAll(filters);
    return raw.map((item) => AjusteContableRegularizacion.fromApi(item));
  },
});
