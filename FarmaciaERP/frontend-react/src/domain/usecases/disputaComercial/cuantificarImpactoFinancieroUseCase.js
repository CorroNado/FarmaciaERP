import { DisputaComercial } from '../../models/DisputaComercial';

// ERP · paso 2.1.2: Cuantificación del Impacto Financiero de la Desviación.
export const cuantificarImpactoFinancieroUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.cuantificar(id);
    return DisputaComercial.fromApi(raw);
  },
});
