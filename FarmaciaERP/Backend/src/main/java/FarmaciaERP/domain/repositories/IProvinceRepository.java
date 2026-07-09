package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Province;
import FarmaciaERP.domain.valueObjects.Ubigeo;

import java.util.List;
import java.util.Optional;

public interface IProvinceRepository {
    List<Province> findAll();
    Optional<Province> findById(Long id);
    Optional<Province> findByUbigeoInei(Ubigeo ubigeo);
    Optional<Province> findByUbigeoReniec(Ubigeo ubigeo);
}
