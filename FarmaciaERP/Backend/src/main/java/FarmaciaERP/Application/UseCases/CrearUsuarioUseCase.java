package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearPacienteRequest;
import FarmaciaERP.Application.DTOs.Request.CrearUsuarioResquest;
import FarmaciaERP.Application.DTOs.Response.CrearPacienteResponse;
import FarmaciaERP.Application.DTOs.Response.CrearUsuarioResponse;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;

import java.util.Optional;

public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public CrearUsuarioResponse ejecutar(CrearUsuarioResquest request) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el gmail: " + request.getEmail());
        }
        return new CrearUsuarioResponse(request.getEmail());
    }
}
