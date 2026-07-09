package FarmaciaERP.domain.entities;

import FarmaciaERP.domain.enums.PermissionModule;
import FarmaciaERP.domain.enums.PermissionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String codigo;
    private String descripcion;
    private PermissionModule modulo;
    private PermissionStatus estado;
}
