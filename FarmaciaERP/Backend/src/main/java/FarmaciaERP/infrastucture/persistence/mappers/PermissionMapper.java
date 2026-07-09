package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Permission;
import FarmaciaERP.infrastucture.persistence.entities.PermissionJPA;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public Permission toDomain(PermissionJPA jpa) {
        return new Permission(
                jpa.getId(),
                jpa.getCodigo(),
                jpa.getDescripcion(),
                jpa.getModulo(),
                jpa.getEstado()
        );
    }
    public PermissionJPA toEntity(Permission permiso){
        return new PermissionJPA(
                permiso.getId(),
                permiso.getCodigo(),
                permiso.getDescripcion(),
                permiso.getModulo(),
                permiso.getEstado()
        );
    }
}
