package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;

public class ClienteMapper {
    public static ClienteJPA ToEntity(Cliente cliente) {
        ClienteJPA entity = new ClienteJPA();
        entity.setId(cliente.getId());
        entity.setNombres(FullnameMapper.toEmbeddable(cliente.getNombres()));
        entity.setDni(DniMapper.toEmbeddable(cliente.getDni()));
        entity.setTipoSeguro(cliente.getTipoSeguro());
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
