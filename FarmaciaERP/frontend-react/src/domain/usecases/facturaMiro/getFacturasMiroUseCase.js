import { FacturaMIRO } from '../../models/FacturaMIRO';

export const getFacturasMiroUseCase = (facturaMiroRepository) => ({
  async execute(filters = {}) {
    const raw = await facturaMiroRepository.getAll(filters);
    return raw.map((item) => FacturaMIRO.fromApi(item));
  },
});
