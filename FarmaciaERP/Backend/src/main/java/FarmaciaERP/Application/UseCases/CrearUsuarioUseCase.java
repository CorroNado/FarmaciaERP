package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearUsuarioResquest;
import FarmaciaERP.Application.DTOs.Response.CrearUsuarioResponse;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public CrearUsuarioResponse ejecutar(CrearUsuarioResquest request) {
        Email email = new Email(request.getEmail());
        FullName fullName = new FullName(request.getNombre(), request.getApellido());
        Optional<Usuario> existente = usuarioRepository.findByEmail(email);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el gmail: " + email.getEmail());
        }
        Usuario saved = new Usuario(
                fullName,
                email,
                passwordEncoder.encode(request.getPassword()),
                RolUsuario.ADMINISTRADOR);
        usuarioRepository.save(saved);
        return new CrearUsuarioResponse(saved.getEmail().getEmail());
    }
}
