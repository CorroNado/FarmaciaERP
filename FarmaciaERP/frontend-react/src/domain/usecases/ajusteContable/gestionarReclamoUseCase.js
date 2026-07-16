import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// Sistema SAP ERP · paso 3.1.a: Gestionar Reclamo con el Laboratorio/Droguería.
export const gestionarReclamoUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.gestionarReclamo(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
