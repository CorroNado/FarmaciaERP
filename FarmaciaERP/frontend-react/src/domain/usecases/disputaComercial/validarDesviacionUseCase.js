import { DisputaComercial } from '../../models/DisputaComercial';

// Comprador · paso 2.1.3: Revisión y Validación de la Desviación.
export const validarDesviacionUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.validarDesviacion(id);
    return DisputaComercial.fromApi(raw);
  },
});
