package FarmaciaERP.application.usecases.usuario;

import FarmaciaERP.application.dto.Request.CrearUsuarioRequest;
import FarmaciaERP.application.dto.Response.CrearUsuarioResponse;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.repositories.IEmailContactRepository;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.domain.valueObjects.usuario.Password;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final IEmailContactRepository emailContactRepository;

    public CrearUsuarioResponse ejecutar(CrearUsuarioRequest request) {
        try {

            FullName fullName = new FullName(request.nombre(), request.apellido());
            Username username = new Username(request.username());
            Password password = new Password(request.password());
            EmailAddress emailAddress = new EmailAddress(request.email());

            Optional<User> existente = usuarioRepository.findByUsername(username);
            if (existente.isPresent()) {
                throw new IllegalArgumentException("Este username ya se encuentra registrado");
            }
            if (emailContactRepository.existsByEmailAddress(emailAddress)) {
                throw new IllegalArgumentException("Este correo electrónico ya está registrado");
            }

            User saved = new User(fullName, request.perfilId(), password, username);
            usuarioRepository.save(saved);

            EmailContact emailContact = new EmailContact(emailAddress);
            User savedWithEmail = usuarioRepository.findByUsername(saved.getUsername()).orElse(null);

            if(savedWithEmail == null)
                throw  new IllegalArgumentException("No se encontro el usuario para guardar el email");

            emailContact = savedWithEmail.añadirEmail(emailContact);
            emailContact.setOwnerDetail(savedWithEmail.getId(),OwnerType.USUARIO);
            emailContactRepository.save(emailContact);

            return new CrearUsuarioResponse(username.getValor(), "User creado exitosamente");
        }catch (IllegalArgumentException e){
            return new CrearUsuarioResponse("Error", e.getMessage());
        }

    }
}
