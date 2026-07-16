import { Pago } from '../../models/Pago';

export const getPagoByIdUseCase = (pagoRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del pago es requerido');
    const raw = await pagoRepository.getById(id);
    return Pago.fromApi(raw);
  },
});
