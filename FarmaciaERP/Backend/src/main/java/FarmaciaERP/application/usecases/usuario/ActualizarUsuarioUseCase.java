package FarmaciaERP.application.usecases.usuario;
import FarmaciaERP.application.dto.Request.ActualizarUsuarioRequest;
import FarmaciaERP.application.dto.Response.ActualizarUsuarioResponse;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import org.springframework.stereotype.Service;

@Service
public class ActualizarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public ActualizarUsuarioUseCase(IUsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
    }

    public ActualizarUsuarioResponse ejecutar(Long id, ActualizarUsuarioRequest request) {
        User usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El usuario con ID " + id + " no existe."));

        usuario.setNombreCompleto(new FullName(request.nombre(), request.apellido()));
        usuario.setUsername(new Username(request.username()));

        usuarioRepository.save(usuario);
        return new ActualizarUsuarioResponse(usuario.getId(), "Usuario actualizado correctamente.");
    }
}
