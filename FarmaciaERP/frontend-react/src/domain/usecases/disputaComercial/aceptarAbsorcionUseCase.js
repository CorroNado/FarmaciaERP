import { DisputaComercial } from '../../models/DisputaComercial';

// Comprador · paso 2.2.2: ¿Se Absorbe la Diferencia Comercial? — Sí.
export const aceptarAbsorcionUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.aceptarAbsorcion(id);
    return DisputaComercial.fromApi(raw);
  },
});
