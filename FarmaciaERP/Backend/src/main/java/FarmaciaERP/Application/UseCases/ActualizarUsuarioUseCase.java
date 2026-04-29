package FarmaciaERP.Application.UseCases;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public ActualizarUsuarioUseCase(IUsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
    }

    public Usuario ejecutar (int id, Usuario usuarioActualizado) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario con ID " + id + " no existe.");
        }
        // Aquí podrías agregar validaciones extra antes de guardar
        return usuarioRepository.save(usuarioActualizado);
    }
}
