package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

public class EliminarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ejecutar(int id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El paciente con ID " + id + " no existe.");
        }
        usuarioRepository.deleteById(id);
    }
}
