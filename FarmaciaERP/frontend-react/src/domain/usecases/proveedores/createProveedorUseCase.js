import { Proveedor } from '../../models/Proveedor';

export const createProveedorUseCase = (proveedorRepository) => ({
  async execute(formData) {
    if (!formData.razonSocial) {
      throw new Error('La razón social es obligatoria');
    }
    if (!formData.ruc) {
      throw new Error('El RUC es obligatorio');
    }
    if (!/^\d{11}$/.test(formData.ruc)) {
      throw new Error('El RUC debe tener 11 dígitos');
    }
    if (formData.contactoEmail) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(formData.contactoEmail)) {
        throw new Error('El formato del correo de contacto no es válido');
      }
    }

    const payload = Proveedor.toApiCreate(formData);
    const raw = await proveedorRepository.create(payload);
    return Proveedor.fromApi(raw);
  },
});
