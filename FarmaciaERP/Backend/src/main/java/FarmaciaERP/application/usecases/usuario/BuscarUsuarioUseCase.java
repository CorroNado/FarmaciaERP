package FarmaciaERP.application.usecases.usuario;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.FullName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BuscarUsuarioUseCase {

    private final IUsuarioRepository usuarioRepository;

    public BuscarUsuarioUseCase (IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<User> byId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<User> all() {
        return usuarioRepository.findAll();
    }


    public List<User> byFullName(FullName nombre) {
        return usuarioRepository.findByName(nombre);
    }

    public List<User> byStatus(UserStatus estado) {
        return usuarioRepository.findByStatus(estado);
    }

    public Optional<User> byEmail(EmailContact emailContact) {
        return usuarioRepository.findByEmail(emailContact);
    }
}
