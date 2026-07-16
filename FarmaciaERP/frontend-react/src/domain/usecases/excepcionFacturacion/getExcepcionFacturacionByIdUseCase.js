import { ExcepcionFacturacion } from '../../models/ExcepcionFacturacion';

export const getExcepcionFacturacionByIdUseCase = (excepcionFacturacionRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la excepción de facturación es requerido');
    const raw = await excepcionFacturacionRepository.getById(id);
    return ExcepcionFacturacion.fromApi(raw);
  },
});
