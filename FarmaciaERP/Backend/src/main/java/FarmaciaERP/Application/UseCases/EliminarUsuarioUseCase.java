package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Infrastucture.Persistence.Mappers.UsuarioMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EliminarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ejecutar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException
                        ("El usuario con ID " + id + " no existe."));
        usuario.setEstado(UsuarioEstados.INACTIVO);
        usuarioRepository.save(usuario);
    }
}
