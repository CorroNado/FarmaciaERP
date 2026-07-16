import { Cliente } from '../../models/Cliente';

export const crearClienteRapidoUseCase = (clienteRepository) => ({
  async execute(formData) {
    if (!formData.nombre || !formData.apellido) {
      throw new Error('Nombre y apellido son requeridos');
    }
    if (!formData.dni || !/^\d{8}$/.test(formData.dni)) {
      throw new Error('El DNI debe tener 8 dígitos');
    }
    const payload = Cliente.toApi(formData);
    await clienteRepository.create(payload);
    // La API solo confirma el DNI creado; recuperamos el cliente completo
    const raw = await clienteRepository.getByDni(formData.dni);
    return raw ? Cliente.fromApi(raw) : null;
  },
});
