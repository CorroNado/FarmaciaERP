package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Profile")
@Getter
@Setter
@NoArgsConstructor
public class ProfileJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_permisos",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private List<PermissionJPA> permisos = new ArrayList<>();

    public ProfileJPA(String name, String description, ProfileStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

}
