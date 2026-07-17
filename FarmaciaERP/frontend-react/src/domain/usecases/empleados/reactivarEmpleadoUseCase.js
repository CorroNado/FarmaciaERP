import { Empleado } from '../../models/Empleado';

export const reactivarEmpleadoUseCase = (empleadoRepository) => ({
  async execute(id, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    const raw = await empleadoRepository.reactivar(id, usuario);
    return Empleado.fromApi(raw);
  },
});
