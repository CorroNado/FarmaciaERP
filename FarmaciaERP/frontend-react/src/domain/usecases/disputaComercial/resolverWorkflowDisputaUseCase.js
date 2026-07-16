import { DisputaComercial } from '../../models/DisputaComercial';

// Workflow ERP · paso 2.3.1: Registrar Resolución — habilita la Fase 03.
export const resolverWorkflowDisputaUseCase = (disputaComercialRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la disputa comercial es requerido');
    const raw = await disputaComercialRepository.resolverWorkflow(id);
    return DisputaComercial.fromApi(raw);
  },
});
