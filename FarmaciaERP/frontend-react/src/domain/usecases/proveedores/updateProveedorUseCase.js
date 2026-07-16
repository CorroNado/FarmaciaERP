import { Proveedor } from '../../models/Proveedor';

export const updateProveedorUseCase = (proveedorRepository) => ({
  async execute(id, formData) {
    if (!id) throw new Error('El id del proveedor es requerido');
    if (!formData.razonSocial) throw new Error('La razón social es obligatoria');

    const payload = Proveedor.toApiUpdate(formData);
    const raw = await proveedorRepository.update(id, payload);
    return Proveedor.fromApi(raw);
  },
});
