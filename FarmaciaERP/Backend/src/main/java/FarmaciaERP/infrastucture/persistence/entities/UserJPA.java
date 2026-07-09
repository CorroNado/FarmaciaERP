package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.TelephoneEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.LoginSecurityEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.PasswordEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.usuario.UsernameEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "App_User")
@Getter
@Setter
@NoArgsConstructor
public class UserJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private UsernameEmb username;

    @Embedded
    private PasswordEmb userPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileJPA perfil;

    @Embedded
    private FullNameEmb nombreCompleto;

    @Embedded
    private LoginSecurityEmb loginSeguro;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime fechaCreacion ;

    @ElementCollection
    @CollectionTable(
            name = "user_telephones",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelephoneEmb> telefonos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "owner_id")
    @SQLRestriction("owner_type = 'USUARIO'")
    private List<EmailContactJPA> emails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "owner_id")
    @SQLRestriction("owner_type = 'USUARIO'")
    private List<AddressJPA> direcciones = new ArrayList<>();

    public UserJPA(UsernameEmb username, PasswordEmb userPassword, ProfileJPA perfil,
                   FullNameEmb nombreCompleto, LoginSecurityEmb loginSeguro,
                   List<TelephoneEmb> telefonos, List<EmailContactJPA> emails, List<AddressJPA> direcciones) {
        this.username = username;
        this.userPassword = userPassword;
        this.perfil = perfil;
        this.nombreCompleto = nombreCompleto;
        this.loginSeguro = loginSeguro;
        this.fechaCreacion = LocalDateTime.now();
        this.telefonos = telefonos;
        this.emails = emails;
        this.direcciones = direcciones;
    }
    public UserJPA(Long userId) {
        this.userId = userId;
        // Usuario de Referencia
    }
}
