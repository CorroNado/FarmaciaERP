import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

export const getAjusteContableByIdUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.getById(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
