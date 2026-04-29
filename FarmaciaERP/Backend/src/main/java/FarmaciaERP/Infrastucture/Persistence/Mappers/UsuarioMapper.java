package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;

public class UsuarioMapper {
    public static UsuarioJPA ToEntity(Usuario usuario) {
        UsuarioJPA entity = new UsuarioJPA();
        entity.setId(usuario.getId());
        entity.setNombres(FullnameMapper.toEmbeddable(usuario.getNombres()));
        entity.setDni(DniMapper.toEmbeddable(usuario.getDni()));
        entity.setTipoSeguro(usuario.getTipoSeguro());
        return entity;
    }

    public static Cliente ToDomain(ClienteJPA entity) {
        return new Cliente(
                entity.getId(),
                FullnameMapper.toDomain(entity.getNombres()),
                DniMapper.toDomain(entity.getDni()),
                entity.getTipoSeguro()
        );
    }
}
