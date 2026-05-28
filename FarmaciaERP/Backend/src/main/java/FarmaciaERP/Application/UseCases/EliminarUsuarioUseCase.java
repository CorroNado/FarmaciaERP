package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Infrastucture.Persistence.Mappers.UsuarioMapper;
import org.springframework.stereotype.Service;
import FarmaciaERP.Application.DTOs.Request.EliminarUsuarioRequest;
import FarmaciaERP.Application.DTOs.Response.EliminarUsuarioResponse;

import java.util.Optional;

@Service
public class EliminarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public EliminarUsuarioResponse ejecutar(EliminarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException
                        ("El usuario con ID " + request.getId() + " no existe."));
        usuario.setEstado(UsuarioEstados.INACTIVO);
        usuarioRepository.save(usuario);
        return new EliminarUsuarioResponse(request.getId(), "Usuario eliminado (estado inactivo) correctamente.");
    }
}
