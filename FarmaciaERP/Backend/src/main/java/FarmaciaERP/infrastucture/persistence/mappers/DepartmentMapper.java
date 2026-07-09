package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Department;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import FarmaciaERP.infrastucture.persistence.entities.DepartmentJPA;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toDomain(DepartmentJPA entity){
        Department domain = new Department();
        domain.setId(entity.getDepartamentoId());
        domain.setNombre(entity.getNombre());
        domain.setUbigeo(ubigeoToDomain(entity.getUbigeo()));
        return domain;
    }

    public DepartmentJPA toEntity(Department domain){
        DepartmentJPA entity = new DepartmentJPA();
        entity.setDepartamentoId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setUbigeo(ubigeoToEmb(domain.getUbigeo()));
        return entity;
    }

    public UbigeoEmb ubigeoToEmb(Ubigeo domain){
        return new UbigeoEmb(
                domain.getUbigeoReniec(),
                domain.getUbigeoInei(),
                domain.getTipo()
        );
    }

    public Ubigeo ubigeoToDomain(UbigeoEmb emb){
        return new Ubigeo(
                emb.getUbigeoReniec(),
                emb.getUbigeoInei(),
                emb.getTipo()
        );
    }
}
