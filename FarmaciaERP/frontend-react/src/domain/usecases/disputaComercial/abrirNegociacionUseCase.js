import { DisputaComercial } from '../../models/DisputaComercial';

// Comprador · paso 2.2.1: Apertura de Disputa con Ejecutivo de Droguería / Laboratorio (nueva ronda).
export const abrirNegociacionUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.abrirNegociacion(id);
    return DisputaComercial.fromApi(raw);
  },
});
