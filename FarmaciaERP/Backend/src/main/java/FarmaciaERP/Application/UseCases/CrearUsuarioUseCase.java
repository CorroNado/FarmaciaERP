package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

import java.util.Optional;

public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.buscarPorGmail(usuario.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el gmail: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }
}
