import { ExcepcionFacturacion } from '../../models/ExcepcionFacturacion';

// Analista AP · paso 1.2: Analizar y Clasificar Discrepancia (dispara notificación 1.3).
export const clasificarExcepcionFacturacionUseCase = (excepcionFacturacionRepository) => ({
  async execute(id, { tipoDiscrepancia }) {
    if (!id) throw new Error('El id de la excepción de facturación es requerido');
    if (!tipoDiscrepancia) {
      throw new Error('Selecciona el tipo de discrepancia (Precio o Cantidad)');
    }

    const payload = ExcepcionFacturacion.toApiClasificar({ tipoDiscrepancia });
    const raw = await excepcionFacturacionRepository.clasificar(id, payload);
    return ExcepcionFacturacion.fromApi(raw);
  },
});
