package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegisterRequest;
import FarmaciaERP.Application.DTOs.Response.RegisterResponse;
import FarmaciaERP.Application.Security.JwtUtils;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
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

        var existe = usuarioRepository.findByEmail(request.getEmail());

        if (existe.isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setConstrasena(passwordEncoder.encode(request.getPassword()));
        usuario.setNombres(request.getNombres());
        usuario.setEstado(UsuarioEstados.ACTIVO);
        usuario.setRole(RolUsuario.ADMINISTRADOR);
        usuario.setRegistro(java.time.LocalDateTime.now());

        var saved = usuarioRepository.save(usuario);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail().getEmail(),
                "Usuario registrado correctamente"
        );
    }
}
