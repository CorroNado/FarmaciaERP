import { Sucursal } from '../../models/Sucursal';

export const getSucursalesUseCase = (sucursalRepository) => ({
  async execute(filters = {}) {
    const raw = await sucursalRepository.getAll(filters);
    return raw.map((item) => Sucursal.fromApi(item));
  },
});
