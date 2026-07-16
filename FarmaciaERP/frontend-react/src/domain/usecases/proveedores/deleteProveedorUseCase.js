export const deleteProveedorUseCase = (proveedorRepository) => ({
  async execute(id) {
    if (!id) throw new Error('El id del proveedor es requerido');
    await proveedorRepository.delete(id);
    return id;
  },
});
