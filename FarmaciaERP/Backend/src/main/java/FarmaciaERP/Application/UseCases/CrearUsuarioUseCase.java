package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

import java.util.Optional;

public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
