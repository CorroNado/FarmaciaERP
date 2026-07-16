import { Devolucion } from '../../models/Devolucion';

export const getDevolucionesUseCase = (devolucionRepository) => ({
  async execute(filters = {}) {
    const raw = await devolucionRepository.getAll(filters);
    return raw.map((item) => Devolucion.fromApi(item));
  },
});
