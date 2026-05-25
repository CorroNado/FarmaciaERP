package FarmaciaERP.application.usecases.usuario;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class EliminarUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ejecutar(Long id) {
        User user = usuarioRepository.findById(id)
                .orElseThrow(()-> new RuntimeException
                        ("El usuario con ID " + id + " no existe."));
        user.setEstado(UserStatus.INACTIVO);
        usuarioRepository.save(user);
    }
}
