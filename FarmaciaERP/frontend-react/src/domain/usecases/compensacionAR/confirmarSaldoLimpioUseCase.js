import { CompensacionAR } from '../../models/CompensacionAR';

export const confirmarSaldoLimpioUseCase = (compensacionARRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la compensación final es requerido');
    const raw = await compensacionARRepository.confirmarSaldo(id);
    return CompensacionAR.fromApi(raw);
  },
});
