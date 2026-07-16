import { RecetaMedicaAR } from '../../models/RecetaMedicaAR';

export const registrarRecetaMedicaARUseCase = (recetaMedicaARRepository) => ({
  async execute({ numero, contabilizacionARId, medicamento, aseguradora, montoDeclarado, montoPreliquidado }) {
    if (!contabilizacionARId) {
      throw new Error('Debe seleccionar el lote consolidado (Fase 02) al que pertenece la receta');
    }
    if (!numero?.trim()) throw new Error('Ingresa el N° de receta médica');
    if (!medicamento?.trim()) throw new Error('Ingresa el medicamento de la receta');
    if (!aseguradora?.trim()) throw new Error('Ingresa la aseguradora de la receta');
    if (!montoDeclarado || Number(montoDeclarado) <= 0) throw new Error('El monto declarado debe ser mayor a cero');

    const payload = RecetaMedicaAR.toApiRegistrar({
      numero, contabilizacionARId, medicamento, aseguradora, montoDeclarado, montoPreliquidado,
    });
    const raw = await recetaMedicaARRepository.registrar(payload);
    return RecetaMedicaAR.fromApi(raw);
  },
});
