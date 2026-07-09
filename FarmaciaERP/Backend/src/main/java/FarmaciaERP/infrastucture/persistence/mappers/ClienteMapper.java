package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import FarmaciaERP.infrastucture.persistence.embeddable.DniEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import FarmaciaERP.infrastucture.persistence.entities.ClienteJPA;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteJPA entity) {
        return new Cliente(
                entity.getId(),
                fullNameToDomain(entity.getNombres()),
                dniToDomain(entity.getDni()),
                entity.getInsuranceType()
        );
    }

    public ClienteJPA toEntity(Cliente cliente) {
        ClienteJPA entity = new ClienteJPA();
        entity.setId(cliente.getId());
        entity.setNombres(fullNameToEmb(cliente.getNombres()));
        entity.setDni(dniToEmb(cliente.getDni()));
        entity.setInsuranceType(cliente.getInsuranceType());
        return entity;
    }

    // — ValueObjects —
    public FullName fullNameToDomain(FullNameEmb fullName) {
        return new FullName(fullName.getNombres(), fullName.getApellidos());
    }

    public FullNameEmb fullNameToEmb(FullName fullName) {
        return new FullNameEmb(fullName.getNombres(), fullName.getApellidos());
    }

    public Dni dniToDomain(DniEmb dni) {
        return new Dni(dni.getDni());
    }

    public DniEmb dniToEmb(Dni dni) {
        return new DniEmb(dni.getValor());
    }
}
