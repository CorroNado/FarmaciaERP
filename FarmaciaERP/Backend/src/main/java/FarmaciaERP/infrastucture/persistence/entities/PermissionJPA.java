package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.PermissionModule;
import FarmaciaERP.domain.enums.PermissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permission_id")
    private Long id;

    @Column(name = "code",nullable = false, unique = true)
    private String codigo;

    @Column(name = "description", nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "module",nullable = false)
    private PermissionModule modulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PermissionStatus estado;

    public PermissionJPA(String codigo, String descripcion,
                         PermissionModule modulo, PermissionStatus estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.modulo = modulo;
        this.estado = estado;
    }
}
