import { DisputaComercial } from '../../models/DisputaComercial';

// Comprador · paso 2.2.2: ¿Se Absorbe la Diferencia Comercial? — No, reabrir para una nueva ronda.
export const reabrirNegociacionUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.reabrirNegociacion(id);
    return DisputaComercial.fromApi(raw);
  },
});
