package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.UserRole;
import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.TelephoneEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.LoginSecurityEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.PasswordEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.UsernameEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
public class UserJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UsernameEmb username;

    @Embedded
    private PasswordEmb userPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileJPA profile;

    @Embedded
    private FullNameEmb fullName;

    @Embedded
    private LoginSecurityEmb loginSecurity;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            name = "usuario_telephones",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<TelephoneEmb> telephones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "email_id")
    private List<EmailContactJPA> emails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "address_id")
    private List<AddressJPA> address = new ArrayList<>();

    public UserJPA(UsernameEmb username, PasswordEmb userPassword, ProfileJPA profile,
                   FullNameEmb fullName, LoginSecurityEmb loginSecurity,
                   List<TelephoneEmb> telephones, List<EmailContactJPA> emails, List<AddressJPA> address) {
        this.username = username;
        this.userPassword = userPassword;
        this.profile = profile;
        this.fullName = fullName;
        this.loginSecurity = loginSecurity;
        this.createdAt = LocalDateTime.now();
        this.telephones = telephones;
        this.emails = emails;
        this.address = address;
    }

}
