package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegisterRequest;
import FarmaciaERP.Application.DTOs.Response.RegisterResponse;
import FarmaciaERP.Application.Security.JwtUtils;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class RegisterUsuarioUseCase {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse execute(@RequestBody RegisterRequest request){
        Email email = new Email(request.getEmail());
        FullName fullName = new FullName(request.getNombre(), request.getApellido());
        var existe = usuarioRepository.findByEmail(email);

        if (existe.isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario(
                fullName,
                email,
                passwordEncoder.encode(request.getPassword()),
                RolUsuario.ADMINISTRADOR
        );

        var saved = usuarioRepository.save(usuario);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail().getEmail(),
                "Usuario registrado correctamente"
        );
    }
}
