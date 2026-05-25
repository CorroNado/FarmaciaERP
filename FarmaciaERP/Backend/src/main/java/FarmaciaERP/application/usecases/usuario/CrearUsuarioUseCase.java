package FarmaciaERP.application.usecases.usuario;

import FarmaciaERP.application.dto.Request.CrearUsuarioResquest;
import FarmaciaERP.application.dto.Response.CrearUsuarioResponse;
import FarmaciaERP.domain.entities.EmailContact;
import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.FullName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CrearUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public CrearUsuarioResponse ejecutar(CrearUsuarioResquest request) {
        EmailContact emailContact = new EmailContact(request.getEmail());
        FullName fullName = new FullName(request.getNombre(), request.getApellido());
        Optional<User> existente = usuarioRepository.findByEmail(emailContact);
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el gmail: " + emailContact.getDireccion());
        }
        User saved = new User(
                fullName,
                emailContact,
                passwordEncoder.encode(request.getPassword()),
                request.getRol());
        usuarioRepository.save(saved);
        return new CrearUsuarioResponse(saved.getEmail().getEmail(),"User creado exitosamente");
    }
}
