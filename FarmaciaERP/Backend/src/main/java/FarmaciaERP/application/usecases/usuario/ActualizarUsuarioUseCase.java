package FarmaciaERP.application.usecases.usuario;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public ActualizarUsuarioUseCase(IUsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
    }

    public User ejecutar (Long id, User userActualizado) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario con ID " + id + " no existe.");
        }
        usuarioRepository.save(userActualizado);
        return userActualizado;
    }
}
