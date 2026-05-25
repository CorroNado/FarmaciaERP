package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.PermissionModule;
import FarmaciaERP.domain.enums.PermissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Permission")
@Getter
@Setter
@NoArgsConstructor
public class PermissionJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionModule module;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionStatus status;

    public PermissionJPA(String code, String description,
                         PermissionModule modulo, PermissionStatus status) {
        this.code = code;
        this.description = description;
        this.module = modulo;
        this.status = status;
    }
}
