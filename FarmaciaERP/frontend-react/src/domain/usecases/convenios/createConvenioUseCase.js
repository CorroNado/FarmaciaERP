import { Convenio } from '../../models/Convenio';

export const createConvenioUseCase = (convenioRepository) => ({
  async execute(formData) {
    if (!formData.numero) {
      throw new Error('El número de contrato marco es obligatorio');
    }
    if (!formData.proveedorId) {
      throw new Error('Debe seleccionar un proveedor homologado');
    }
    if (!formData.fechaInicio || !formData.fechaFin) {
      throw new Error('La vigencia del convenio (inicio y fin) es obligatoria');
    }
    if (formData.fechaFin < formData.fechaInicio) {
      throw new Error('La fecha de fin no puede ser anterior a la fecha de inicio');
    }
    if (!formData.itemsPactados || formData.itemsPactados.length === 0) {
      throw new Error('RN-MM-004: debe pactar al menos un ítem con precio congelado (Info-Record)');
    }
    for (const item of formData.itemsPactados) {
      if (!item.medicamentoId) {
        throw new Error('Cada ítem pactado debe referenciar un medicamento');
      }
      if (!item.precioPactado || Number(item.precioPactado) <= 0) {
        throw new Error('El precio pactado debe ser mayor a 0');
      }
    }

    const payload = Convenio.toApiCreate(formData);
    const raw = await convenioRepository.create(payload);
    return Convenio.fromApi(raw);
  },
});
