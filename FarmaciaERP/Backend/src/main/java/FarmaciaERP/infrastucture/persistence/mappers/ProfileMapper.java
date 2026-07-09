package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Profile;
import FarmaciaERP.infrastucture.persistence.entities.ProfileJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileMapper {
    private final PermissionMapper permissionMapper;

    public Profile ToDomain(ProfileJPA jpa){
        return new Profile(
                jpa.getPerfilId(),
                jpa.getNombre(),
                jpa.getDescripcion(),
                jpa.getEstado(),
                jpa.getPermisos().stream().map(permissionMapper::toDomain).toList()
        );
    }
    public ProfileJPA ToEntity(Profile profile){
        return new ProfileJPA(
                profile.getId(),
                profile.getNombre(),
                profile.getDescripcion(),
                profile.getEstado(),
                profile.getPermisos().stream().map(permissionMapper::toEntity).toList()
        );
    }
}
