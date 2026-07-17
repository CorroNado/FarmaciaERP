import { Empleado } from '../../models/Empleado';

export const darBajaInmediataUseCase = (empleadoRepository) => ({
  async execute(id, { motivo, turnoInfo }, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    if (!motivo || !motivo.trim()) throw new Error('Debe ingresar un motivo para la baja inmediata');
    const raw = await empleadoRepository.bajaInmediata(id, { motivo, turnoInfo }, usuario);
    return Empleado.fromApi(raw);
  },
});
