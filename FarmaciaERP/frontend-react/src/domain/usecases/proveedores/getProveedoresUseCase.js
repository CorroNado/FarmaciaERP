import { Proveedor } from '../../models/Proveedor';

export const getProveedoresUseCase = (proveedorRepository) => ({
  async execute(filters = {}) {
    const raw = await proveedorRepository.getAll(filters);
    return raw.map((item) => Proveedor.fromApi(item));
  },
});
