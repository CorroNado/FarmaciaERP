package FarmaciaERP.application.usecases.usuario;
import FarmaciaERP.application.dto.Response.UserListResponse;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.EmailAddress;
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

    public Optional<UserListResponse> byId(Long id) {
        return usuarioRepository.findById(id).stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername().getValor(),
                        user.getNombreCompleto().getValor(),
                        user.getPerfilId(),
                        user.getLoginSeguro().getEstado().name(),
                        user.getFechaCreacion()
                ))
                .findFirst();
    }

    public List<UserListResponse> all() {
        return usuarioRepository.findAll().stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername().getValor(),
                        user.getNombreCompleto().getValor(),
                        user.getPerfilId(),
                        user.getLoginSeguro().getEstado().name(),
                        user.getFechaCreacion()
                ))
                .toList();
    }


    public List<UserListResponse> byFullName(FullName nombre) {
        return usuarioRepository.findByFullName(nombre).stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername().getValor(),
                        user.getNombreCompleto().getValor(),
                        user.getPerfilId(), // Nombre del rol/perfil
                        user.getLoginSeguro().getEstado().name(),
                        user.getFechaCreacion()
                ))
                .toList();
    }

    public List<UserListResponse> byStatus(UserStatus estado) {
        return usuarioRepository.findByStatus(estado).stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername().getValor(),
                        user.getNombreCompleto().getValor(),
                        user.getPerfilId(), // Nombre del rol/perfil
                        user.getLoginSeguro().getEstado().name(),
                        user.getFechaCreacion()
                ))
                .toList();
    }

    public Optional<UserListResponse> byEmail(EmailAddress emailAddress) {
        return usuarioRepository.findByEmail(emailAddress).stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getUsername().getValor(),
                        user.getNombreCompleto().getValor(),
                        user.getPerfilId(), // Nombre del rol/perfil
                        user.getLoginSeguro().getEstado().name(),
                        user.getFechaCreacion()
                ))
                .findFirst();
    }
}
