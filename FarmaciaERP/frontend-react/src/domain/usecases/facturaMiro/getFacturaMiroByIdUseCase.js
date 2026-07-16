import { FacturaMIRO } from '../../models/FacturaMIRO';

export const getFacturaMiroByIdUseCase = (facturaMiroRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id de la factura MIRO es requerido');
    const raw = await facturaMiroRepository.getById(id);
    return FacturaMIRO.fromApi(raw);
  },
});
