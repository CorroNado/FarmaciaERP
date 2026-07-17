import { Empleado } from '../../models/Empleado';

export const getEmpleadosUseCase = (empleadoRepository) => ({
  async execute(filters = {}) {
    const raw = await empleadoRepository.getAll(filters);
    return raw.map((item) => Empleado.fromApi(item));
  },
});
