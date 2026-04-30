package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;

public class UsuarioMapper {
    public static UsuarioJPA ToEntity(Usuario usuario) {
        UsuarioJPA entity = new UsuarioJPA();
        entity.setNombres(FullnameMapper.toEmbeddable(usuario.getNombres()));
        entity.setEmail(EmailMapper.toEmbeddable(usuario.getEmail()));
        entity.setPassword(usuario.getConstrasena());
        entity.setRol(usuario.getRole());
        entity.setEstado(usuario.getEstado());
        return entity;
    }

    public static Usuario ToDomain(UsuarioJPA entity) {
        return new Usuario(
                FullnameMapper.toDomain(entity.getNombres()),
                EmailMapper.toDomain(entity.getEmail()),
                entity.getPassword(),
                entity.getRol(),
                entity.getEstado(),
                entity.getRegistro()
        );
    }
}
