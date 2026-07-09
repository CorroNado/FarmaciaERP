package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.TelephoneType;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.domain.valueObjects.Telephone;
import FarmaciaERP.domain.valueObjects.usuario.LoginSecurity;
import FarmaciaERP.domain.valueObjects.usuario.Password;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.TelephoneEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.LoginSecurityEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.PasswordEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.UsernameEmb;
import FarmaciaERP.infrastucture.persistence.entities.ProfileJPA;
import FarmaciaERP.infrastucture.persistence.entities.UserJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final EmailContactMapper emailContactMapper;
    private final AddressMapper addressMapper;

    public User toDomain(UserJPA jpa) {
        return new User(
                jpa.getUserId(),
                usernameToDomain(jpa.getUsername()),
                passwordToDomain(jpa.getUserPassword()),
                jpa.getPerfil().getPerfilId(),
                fullNameToDomain(jpa.getNombreCompleto()),
                loginSecurityToDomain(jpa.getLoginSeguro()),
                jpa.getFechaCreacion(),
                jpa.getEmails().stream()
                        .map(emailContactMapper::toDomain)
                        .toList(),
                jpa.getDirecciones().stream().
                        map(addressMapper::toDomain)
                        .toList(),
                jpa.getTelefonos().stream()
                        .map(this::telephoneToDomain)
                        .toList()
        );
    }

    public  UserJPA toEntity(User domain) {
        return new UserJPA(
                usernameToEmb(domain.getUsername()),
                passwordToEmb(domain.getUserPassword()),
                new ProfileJPA(domain.getPerfilId()),                                  // necesita el JPA ya cargado
                fullNameToEmb(domain.getNombreCompleto()),
                loginSecurityToEmb(domain.getLoginSeguro()),
                domain.getTelefonos() != null? new ArrayList<>(
                        domain.getTelefonos().stream()
                        .map(this::telephoneToEmb)
                        .toList()): new ArrayList<>(),
                domain.getEmailContacts()!= null? new ArrayList<>(
                        domain.getEmailContacts().stream()
                        .map(emailContactMapper::toJPA)
                        .toList()): new ArrayList<>(),
                domain.getDirecciones()!=null? new ArrayList<>(
                        domain.getDirecciones().stream()
                        .map(addressMapper::toJPA)
                        .toList()): new ArrayList<>()
        );
    }

    // — Embeddables —
    public Username usernameToDomain(UsernameEmb username) {
        return new Username(username.getValor());
    }
    public UsernameEmb usernameToEmb(Username username) {
        return new UsernameEmb(username.getValor());
    }

    public Password passwordToDomain(PasswordEmb password) {
        return Password.setHashPassword(password.getValor());
    }
    public PasswordEmb passwordToEmb(Password password) {
        return new PasswordEmb(password.getValor());
    }

    public FullName fullNameToDomain(FullNameEmb fullName) {
        return new FullName(fullName.getNombres(), fullName.getApellidos());
    }
    public FullNameEmb fullNameToEmb(FullName fullName) {
        return new FullNameEmb(fullName.getNombres(), fullName.getApellidos());
    }

    public LoginSecurity loginSecurityToDomain(LoginSecurityEmb loginSecurity) {
        return new LoginSecurity(
                loginSecurity.getIntentosLogin(),
                loginSecurity.getTiempoDesbloqueo(),
                loginSecurity.getEstado()
        );
    }
    public LoginSecurityEmb loginSecurityToEmb(LoginSecurity loginSecurity) {
        return new LoginSecurityEmb(
                loginSecurity.getIntentosLogin(),
                loginSecurity.getTiempoDesbloqueo(),
                loginSecurity.getEstado()
        );
    }

    // — Telephone —

    private Telephone telephoneToDomain(TelephoneEmb emb) {
        if (emb.getTipo() == TelephoneType.CELULAR) {
            return new Telephone(emb.getPrefijo(), emb.getDescripcion(), emb.getNumero());
        }
        return new Telephone(emb.getPrefijo(), emb.getCodigoArea(),
                emb.getNumero(), emb.getDescripcion(), emb.getTipo());
    }

    public TelephoneEmb telephoneToEmb(Telephone domain) {
        return new TelephoneEmb(
                domain.getPrefijo(),
                domain.getCodigoArea(),
                domain.getNumero(),
                domain.getDescripcion(),
                domain.getTipo()
        );
    }
}