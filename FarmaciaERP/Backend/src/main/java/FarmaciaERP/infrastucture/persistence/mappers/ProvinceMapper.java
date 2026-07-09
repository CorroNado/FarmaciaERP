package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.Province;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import FarmaciaERP.infrastucture.persistence.entities.DepartmentJPA;
import FarmaciaERP.infrastucture.persistence.entities.ProvinceJPA;
import org.springframework.stereotype.Component;

@Component
public class ProvinceMapper {

    public Province toDomain(ProvinceJPA entity){
        Province domain = new Province();
        domain.setId(entity.getProvinciaId());
        domain.setNombre(entity.getNombre());
        domain.setUbigeo(ubigeoToDomain(entity.getUbigeo()));
        domain.setDepartmentId(entity.getDepartamento().getDepartamentoId());
        return domain;
    }

    public ProvinceJPA toEntity(Province domain){
        ProvinceJPA entity = new ProvinceJPA();
        entity.setProvinciaId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setUbigeo(ubigeoToEmb(domain.getUbigeo()));
        entity.setDepartamento(new DepartmentJPA(domain.getDepartmentId()));
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
