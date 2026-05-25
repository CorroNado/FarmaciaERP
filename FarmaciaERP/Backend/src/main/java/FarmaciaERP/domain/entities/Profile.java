package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.ProfileStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Profile {
    private Long id;
    private String nombre;
    private String descripcion;
    private ProfileStatus status;
    private List<Permission> permisos;

    public void addPermission(Permission permiso) {
        boolean exists = this.permisos.stream()
                .anyMatch(p -> p.getCode().equals(permiso.getCode()));
        if (exists)
            throw new IllegalArgumentException("Permiso ya existe en el perfil");
        this.permisos.add(permiso);
    }

    public void removePermission(Long permisoId) {
        Permission permiso = this.permisos.stream()
                .filter(p -> p.getId().equals(permisoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado"));
        this.permisos.remove(permiso);
    }
}
