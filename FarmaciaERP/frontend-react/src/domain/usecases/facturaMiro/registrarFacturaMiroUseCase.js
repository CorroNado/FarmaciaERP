import { FacturaMIRO } from '../../models/FacturaMIRO';

export const registrarFacturaMiroUseCase = (facturaMiroRepository) => ({
  async execute({ ordenCompraId, numeroFactura, fechaEmision }) {
    if (!ordenCompraId) {
      throw new Error('Debe seleccionar una Orden de Compra con entrada de mercancía (MIGO) registrada');
    }
    if (!numeroFactura || !numeroFactura.trim()) {
      throw new Error('El N° de factura es obligatorio');
    }
    if (!fechaEmision || !fechaEmision.trim()) {
      throw new Error('La fecha de emisión es obligatoria');
    }

    const payload = FacturaMIRO.toApiRegistrar({ ordenCompraId, numeroFactura, fechaEmision });
    const raw = await facturaMiroRepository.registrar(payload);
    return FacturaMIRO.fromApi(raw);
  },
});
