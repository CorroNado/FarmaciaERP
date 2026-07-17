import { Empleado } from '../../models/Empleado';

export const programarBajaUseCase = (empleadoRepository) => ({
  async execute(id, { fechaEfectiva, observacion, turnoInfo }, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    if (!fechaEfectiva) throw new Error('La fecha efectiva de la baja programada es obligatoria');
    const raw = await empleadoRepository.bajaProgramada(id, { fechaEfectiva, observacion, turnoInfo }, usuario);
    return Empleado.fromApi(raw);
  },
});
