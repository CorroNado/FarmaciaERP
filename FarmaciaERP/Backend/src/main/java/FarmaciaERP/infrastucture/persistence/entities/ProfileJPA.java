package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_id")
    private Long perfilId;

    @Column(name = "name", nullable = false, unique = true)
    private String nombre;

    @Column(name= "description", nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name= "status", nullable = false)
    private ProfileStatus estado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "perfil_permisos",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionJPA> permisos = new ArrayList<>();

    public ProfileJPA(String nombre, String descripcion, ProfileStatus estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public ProfileJPA(Long perfilId) {
        this.perfilId = perfilId;
    }
}
