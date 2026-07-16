import { ExcepcionFacturacion } from '../../models/ExcepcionFacturacion';

// Sistema ERP: captura automáticamente la excepción desde una conciliación bloqueada en MRBR.
export const capturarExcepcionFacturacionUseCase = (excepcionFacturacionRepository) => ({
  async execute({ conciliacionTresViasId }) {
    if (!conciliacionTresViasId) {
      throw new Error('Debe seleccionarse la conciliación de 3 vías bloqueada en MRBR');
    }

    const payload = ExcepcionFacturacion.toApiCapturar({ conciliacionTresViasId });
    const raw = await excepcionFacturacionRepository.capturar(payload);
    return ExcepcionFacturacion.fromApi(raw);
  },
});
