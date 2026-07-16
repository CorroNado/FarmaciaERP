import { CierreCaja } from '../../models/CierreCaja';

export const clasificarCopagoCoberturaUseCase = (cierreCajaRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del cierre de caja es requerido');
    const raw = await cierreCajaRepository.clasificarCopagoCobertura(id);
    return CierreCaja.fromApi(raw);
  },
});
