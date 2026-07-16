import { Cliente } from '../../models/Cliente';

export const buscarClientePorDniUseCase = (clienteRepository) => ({
  async execute(dni) {
    if (!dni || !/^\d{8}$/.test(dni)) {
      throw new Error('Ingresa un DNI válido de 8 dígitos');
    }
    const raw = await clienteRepository.getByDni(dni);
    return raw ? Cliente.fromApi(raw) : null;
  },
});
