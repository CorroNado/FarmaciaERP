import { CierreCaja } from '../../models/CierreCaja';

export const registrarJustificacionUseCase = (cierreCajaRepository) => ({
  async execute(id, { justificacion }) {
    if (!id) throw new Error('El id del cierre de caja es requerido');
    if (!justificacion || !justificacion.trim()) {
      throw new Error('Debes registrar una justificación antes de continuar');
    }

    const payload = CierreCaja.toApiJustificacion({ justificacion });
    const raw = await cierreCajaRepository.registrarJustificacion(id, payload);
    return CierreCaja.fromApi(raw);
  },
});
