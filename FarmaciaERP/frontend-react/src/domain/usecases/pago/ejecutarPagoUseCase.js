import { Pago } from '../../models/Pago';

export const ejecutarPagoUseCase = (pagoRepository) => ({
  async execute({ facturaMIROId, banco, fechaPago }) {
    if (!facturaMIROId) {
      throw new Error('Debe seleccionar una factura (MIRO) con conciliación de 3 vías exitosa');
    }
    if (!banco || !banco.trim()) {
      throw new Error('RN-F09-003: el banco/cuenta de origen es obligatorio');
    }
    if (!fechaPago || !fechaPago.trim()) {
      throw new Error('RN-F09-003: la fecha de pago es obligatoria');
    }

    const payload = Pago.toApiEjecutar({ facturaMIROId, banco, fechaPago });
    const raw = await pagoRepository.ejecutar(payload);
    return Pago.fromApi(raw);
  },
});
