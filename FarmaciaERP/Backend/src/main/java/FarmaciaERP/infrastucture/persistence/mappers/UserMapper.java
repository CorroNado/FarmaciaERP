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

import java.util.ArrayList;

public class UserMapper {
    public static User toDomain(UserJPA jpa) {
        return new User(
                jpa.getId(),
                new Username(jpa.getUsername().getValue()),
                new Password(jpa.getUserPassword().getValue()),
                ProfileMapper.toDomain(jpa.getPerfil()),   // reemplaza UserRole
                new FullName(jpa.getFullName().getFirstName(), jpa.getFullName().getLastName()),
                new LoginSecurity(
                        jpa.getLoginSecurity().getLoginAttempts(),
                        jpa.getLoginSecurity().getLockUntil(),
                        jpa.getLoginSecurity().getEstado()
                ),
                jpa.getCreatedAt(),
                jpa.getAddress().stream()
                        .map(AddressMapper::toDomain)
                        .toList(),
                jpa.getEmails().stream()
                        .map(EmailContactMapper::toDomain)
                        .toList(),
                jpa.getTelephones().stream()
                        .map(UserMapper::telephoneToDomain)
                        .toList()
        );
    }

    public static UserJPA toJPA(User domain, ProfileJPA profileJPA) {
        return new UserJPA(
                usernameToEmb(domain.getUsername()),
                passwordToEmb(domain.getUserPassword()),
                profileJPA,                                  // necesita el JPA ya cargado
                fullNameToEmb(domain.getFullName()),
                loginSecurityToEmb(domain.getLoginSecurity()),
                domain.getTelephones().stream()
                        .map(UserMapper::telephoneToEmb)
                        .toList(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    // — Embeddables —

    public static UsernameEmb usernameToEmb(Username username) {
        return new UsernameEmb(username.getValor());
    }

    public static PasswordEmb passwordToEmb(Password password) {
        return new PasswordEmb(password.getValor());
    }

    public static FullNameEmb fullNameToEmb(FullName fullName) {
        return new FullNameEmb(fullName.getNombres(), fullName.getApellidos());
    }

    public static LoginSecurityEmb loginSecurityToEmb(LoginSecurity loginSecurity) {
        return new LoginSecurityEmb(
                loginSecurity.getIntentosLogin(),
                loginSecurity.getTiempoDesbloqueo(),
                loginSecurity.getEstado()
        );
    }

    // — Telephone —

    private static Telephone telephoneToDomain(TelephoneEmb emb) {
        if (emb.getTipo() == TelephoneType.CELULAR) {
            return new Telephone(emb.getPrefijo(), emb.getDescripcion(), emb.getNumero());
        }
        return new Telephone(emb.getPrefijo(), emb.getCodigoArea(),
                emb.getNumero(), emb.getDescripcion(), emb.getTipo());
    }

    public static TelephoneEmb telephoneToEmb(Telephone domain) {
        return new TelephoneEmb(
                domain.getPrefijo(),
                domain.getCodigoArea(),
                domain.getNumero(),
                domain.getDescripcion(),
                domain.getTipo()
        );
    }
}