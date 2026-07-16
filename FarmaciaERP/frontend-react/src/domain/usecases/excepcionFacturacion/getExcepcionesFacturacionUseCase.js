import { ExcepcionFacturacion } from '../../models/ExcepcionFacturacion';

export const getExcepcionesFacturacionUseCase = (excepcionFacturacionRepository) => ({
  async execute(filters = {}) {
    const raw = await excepcionFacturacionRepository.getAll(filters);
    return raw.map((item) => ExcepcionFacturacion.fromApi(item));
  },
});
