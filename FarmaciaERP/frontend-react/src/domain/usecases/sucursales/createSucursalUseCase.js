import { Sucursal } from '../../models/Sucursal';

export const createSucursalUseCase = (sucursalRepository) => ({
  async execute({ codigo, nombre }) {
    if (!codigo || !codigo.trim()) {
      throw new Error('El código de la sucursal es obligatorio');
    }
    if (!nombre || !nombre.trim()) {
      throw new Error('El nombre de la sucursal es obligatorio');
    }
    const payload = Sucursal.toApiCrear({ codigo, nombre });
    const raw = await sucursalRepository.crear(payload);
    return Sucursal.fromApi(raw);
  },
});
