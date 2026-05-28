package FarmaciaERP.Application.UseCases;
import org.springframework.stereotype.Service;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Application.DTOs.Request.ActualizarUsuarioRequest;
import FarmaciaERP.Application.DTOs.Response.ActualizarUsuarioResponse;
import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Domain.ValueObjects.Email;

@Service
public class ActualizarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public ActualizarUsuarioUseCase(IUsuarioRepository userRepository) {
        this.usuarioRepository = userRepository;
    }

    public ActualizarUsuarioResponse ejecutar ( Long  id,ActualizarUsuarioRequest request) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("El usuario con ID " + id + " no existe.");
        }
        // Aquí podrías agregar validaciones extra antes de guardar
        Usuario actualizado = new Usuario(
            id,
            new FullName(request.getNombre(), request.getApellido()),
            new Email(request.getEmail()),
            request.getPassword(),
            request.getRole(),
            request.getEstado(),
            request.getRegistro(),
            request.getLoginAttempts(),
            request.getLockUntil()
        );
        usuarioRepository.save(actualizado);
        return new ActualizarUsuarioResponse(id, "Usuario actualizado correctamente.");
    }
}
