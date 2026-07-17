import { Empleado } from '../../models/Empleado';

export const createEmpleadoUseCase = (empleadoRepository) => ({
  async execute(formData, usuario) {
    if (!formData.apellidoPaterno) throw new Error('El apellido paterno es obligatorio');
    if (!formData.apellidoMaterno) throw new Error('El apellido materno es obligatorio');
    if (!formData.nombres) throw new Error('Los nombres son obligatorios');
    if (!formData.dni) throw new Error('El DNI es obligatorio');
    if (!formData.rol) throw new Error('El rol es obligatorio');
    if (Number(formData.salario) < 0) throw new Error('El salario debe ser un número positivo');

    const payload = Empleado.toApiCreate(formData);
    const raw = await empleadoRepository.create(payload, usuario);
    return Empleado.fromApi(raw);
  },
});
