package FarmaciaERP.Application.UseCases;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;

    public ActualizarUsuarioUseCase(IUsuarioRepository userRepository, PasswordEncoder encoder) {
        this.encoder=encoder;
        this.usuarioRepository = userRepository;
    }

    public ActualizarUsuarioResponse ejecutar ( Long  id,ActualizarUsuarioRequest request) {
       Usuario  actual = usuarioRepository.findById(id).orElseThrow(()->new RuntimeException("El usuario con ID " + id + " no existe."));
        // Aquí podrías agregar validaciones extra antes de guardar
        actual.setNombres(new FullName(request.getNombre(), request.getApellido()));
        actual.setEmail(new Email(request.getEmail()));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            actual.setPassword(encoder.encode(request.getPassword()));
        }
        actual.setRole(request.getRole());
        actual.setEstado(request.getEstado());
        usuarioRepository.save(actual);
        return new ActualizarUsuarioResponse(id, "Usuario actualizado correctamente.");
    }
}
