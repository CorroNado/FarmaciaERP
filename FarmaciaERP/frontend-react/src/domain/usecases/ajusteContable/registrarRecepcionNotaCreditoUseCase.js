import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// Área Financiera · paso 3.1: ¿Se Recibe Nota de Crédito? (Sí / No).
export const registrarRecepcionNotaCreditoUseCase = (ajusteContableRepository) => ({
  async execute(id, { recibida }) {
    if (!id) throw new Error('El id del ajuste contable es requerido');

    const payload = AjusteContableRegularizacion.toApiRecepcionNotaCredito({ recibida });
    const raw = await ajusteContableRepository.recepcionNotaCredito(id, payload);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
