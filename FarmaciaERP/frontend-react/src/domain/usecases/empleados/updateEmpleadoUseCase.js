import { Empleado } from '../../models/Empleado';

export const updateEmpleadoUseCase = (empleadoRepository) => ({
  async execute(id, formData, usuario) {
    if (!id) throw new Error('El id del colaborador es requerido');
    if (!formData.apellidoPaterno) throw new Error('El apellido paterno es obligatorio');
    if (!formData.apellidoMaterno) throw new Error('El apellido materno es obligatorio');
    if (!formData.nombres) throw new Error('Los nombres son obligatorios');
    if (!formData.dni) throw new Error('El DNI es obligatorio');

    const payload = Empleado.toApiUpdate(formData);
    const raw = await empleadoRepository.update(id, payload, usuario);
    return Empleado.fromApi(raw);
  },
});
