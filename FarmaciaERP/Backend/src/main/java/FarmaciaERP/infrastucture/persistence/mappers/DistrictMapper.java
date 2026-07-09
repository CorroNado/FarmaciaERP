package FarmaciaERP.infrastucture.persistence.mappers;


import FarmaciaERP.domain.entities.District;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import FarmaciaERP.infrastucture.persistence.entities.DistrictJPA;
import FarmaciaERP.infrastucture.persistence.entities.ProvinceJPA;
import org.springframework.stereotype.Component;

@Component
public class DistrictMapper {
    public District toDomain(DistrictJPA entity){
        District domain = new District();
        domain.setId(entity.getDistritoId());
        domain.setNombre(entity.getNombre());
        domain.setUbigeo(ubigeoToDomain(entity.getUbigeo()));
        domain.setProvinceId(entity.getProvincia().getProvinciaId());
        return domain;
    }

    public DistrictJPA toEntity(District domain){
        DistrictJPA entity = new DistrictJPA();
        entity.setDistritoId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setUbigeo(ubigeoToEmb(domain.getUbigeo()));
        entity.setProvincia(new ProvinceJPA(domain.getProvinceId()));
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
