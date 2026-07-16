package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;

public class ProveedorMapper {

    public static ProveedorJPA ToEntity(Proveedor proveedor) {
        ProveedorJPA entity = new ProveedorJPA();
        if (proveedor.getId() != null) {
            entity.setId(proveedor.getId());
        }
        entity.setRazonSocial(proveedor.getRazonSocial());
        entity.setRuc(proveedor.getRuc());
        entity.setContactoEmail(proveedor.getContactoEmail());
        entity.setContactoTelefono(proveedor.getContactoTelefono());
        entity.setEstado(proveedor.getEstado());
        return entity;
    }

    public static Proveedor ToDomain(ProveedorJPA entity) {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(entity.getId());
        proveedor.setRazonSocial(entity.getRazonSocial());
        proveedor.setRuc(entity.getRuc());
        proveedor.setContactoEmail(entity.getContactoEmail());
        proveedor.setContactoTelefono(entity.getContactoTelefono());
        proveedor.setEstado(entity.getEstado());
        return proveedor;
    }
}
