package FarmaciaERP.application.usecases.auth;

import FarmaciaERP.application.dto.Request.RegisterRequest;
import FarmaciaERP.application.dto.Response.RegisterResponse;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserRole;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.FullName;
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
        EmailContact emailContact = new EmailContact(request.getEmail());
        FullName fullName = new FullName(request.getNombre(), request.getApellido());
        var existe = usuarioRepository.findByEmail(emailContact);

        if (existe.isPresent()) {
            throw new RuntimeException("El direccion ya está registrado");
        }

        User user = new User(
                fullName,
                emailContact,
                passwordEncoder.encode(request.getPassword()),
                UserRole.ADMINISTRADOR
        );

        var saved = usuarioRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail().getEmail(),
                "User registrado correctamente"
        );
    }
}
