package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearPacienteRequest;
import FarmaciaERP.Application.DTOs.Request.CrearUsuarioResquest;
import FarmaciaERP.Application.DTOs.Response.CrearPacienteResponse;
import FarmaciaERP.Application.DTOs.Response.CrearUsuarioResponse;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
                request.getPassword(),
                RolUsuario.ADMINISTRADOR);
        usuarioRepository.save(saved);
        return new CrearUsuarioResponse(saved.getEmail().getEmail());
    }
}
