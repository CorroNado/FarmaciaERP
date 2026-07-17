import { Empleado } from '../../models/Empleado';

export const darBajaSinTurnosUseCase = (empleadoRepository) => ({
  async execute(id, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    const raw = await empleadoRepository.bajaSinTurnos(id, usuario);
    return Empleado.fromApi(raw);
  },
});
