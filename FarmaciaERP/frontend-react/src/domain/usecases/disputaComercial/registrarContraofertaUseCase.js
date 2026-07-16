import { DisputaComercial } from '../../models/DisputaComercial';

// Laboratorio / Droguería · paso 2.2.1 (cont.): envía su contrapropuesta de la ronda vigente.
export const registrarContraofertaUseCase = (disputaComercialRepository) => ({
  async execute(id, { montoContraoferta }) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    if (!montoContraoferta || Number(montoContraoferta) <= 0) {
      throw new Error('El monto de la contrapropuesta debe ser mayor a cero');
    }

    const payload = DisputaComercial.toApiContraoferta({ montoContraoferta });
    const raw = await disputaComercialRepository.contraoferta(id, payload);
    return DisputaComercial.fromApi(raw);
  },
});
