import { SugerenciaMRPItem } from '../../models/SolicitudPedido';

export const calcularMRPUseCase = (solPedRepository) => ({
  async execute(items) {
    if (!items || items.length === 0) {
      throw new Error('Selecciona al menos un medicamento para calcular la sugerencia MRP');
    }
    for (const item of items) {
      if (!item.medicamentoId) {
        throw new Error('Cada línea debe referenciar un medicamento');
      }
      if (item.stockMinimo === '' || Number(item.stockMinimo) < 0) {
        throw new Error('El stock mínimo debe ser un número válido');
      }
    }
    const payload = {
      items: items.map((i) => ({
        medicamentoId: Number(i.medicamentoId),
        stockMinimo: Number(i.stockMinimo),
      })),
    };
    const raw = await solPedRepository.calcularMRP(payload);
    return raw.map((item) => SugerenciaMRPItem.fromApi(item));
  },
});
