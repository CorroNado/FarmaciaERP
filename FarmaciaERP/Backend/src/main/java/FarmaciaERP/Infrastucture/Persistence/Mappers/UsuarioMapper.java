package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;

public class UsuarioMapper {
    public static UsuarioJPA ToEntity(Usuario usuario) {
        UsuarioJPA entity = new UsuarioJPA();
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());
        entity.setPassword(usuario.getPassword());
        entity.setEstado(usuario.getEstado());
        return entity;
    }

    public static Usuario ToDomain(UsuarioJPA entity) {
        return new Usuario(
                entity.getNombre(),
                entity.getEmail(),
                entity.getPassword()
        );
    }
}
