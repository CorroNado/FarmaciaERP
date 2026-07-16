import { DispersionBancariaCierre } from '../../models/DispersionBancariaCierre';

// 6.3 - Generar Archivo Bancario Plano (IDoc) (Sistema ERP)
export const generarArchivoBancarioUseCase = (dispersionBancariaRepository) => ({
  async execute(id) {
    const raw = await dispersionBancariaRepository.generarArchivo(id);
    return DispersionBancariaCierre.fromApi(raw);
  },
});
