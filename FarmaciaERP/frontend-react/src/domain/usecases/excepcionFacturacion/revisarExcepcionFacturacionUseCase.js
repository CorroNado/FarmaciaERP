import { ExcepcionFacturacion } from '../../models/ExcepcionFacturacion';

// Analista AP · paso 1.1: Revisar Panel de Facturas Bloqueadas.
export const revisarExcepcionFacturacionUseCase = (excepcionFacturacionRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la excepción de facturación es requerido');

    const raw = await excepcionFacturacionRepository.revisar(id);
    return ExcepcionFacturacion.fromApi(raw);
  },
});
