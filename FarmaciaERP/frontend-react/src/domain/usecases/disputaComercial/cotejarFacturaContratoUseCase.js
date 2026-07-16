import { DisputaComercial } from '../../models/DisputaComercial';

// Comprador · paso 2.1.1: Extracción y Cotejo de Datos de Facturación vs. Acuerdos.
export const cotejarFacturaContratoUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.cotejar(id);
    return DisputaComercial.fromApi(raw);
  },
});
