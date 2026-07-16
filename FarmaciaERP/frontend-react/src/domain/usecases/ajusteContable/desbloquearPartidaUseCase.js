import { AjusteContableRegularizacion } from '../../models/AjusteContableRegularizacion';

// FI-AP · paso 3.3: Desbloquear Partida Presupuestaria y Actualizar Estado de Pago — habilita la Fase 04.
export const desbloquearPartidaUseCase = (ajusteContableRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del ajuste contable es requerido');
    const raw = await ajusteContableRepository.desbloquearPartida(id);
    return AjusteContableRegularizacion.fromApi(raw);
  },
});
