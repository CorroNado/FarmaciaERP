package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.PermissionModule;
import FarmaciaERP.domain.enums.PermissionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Permission {
    private Long id;
    private String code;
    private String description;
    private PermissionModule module;
    private PermissionStatus status;
}
