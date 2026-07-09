package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.District;
import FarmaciaERP.domain.valueObjects.Ubigeo;

import java.util.List;
import java.util.Optional;

public interface IDistrictRepository {
    List<District> findAll();
    Optional<District> findById(Long id);
    Optional<District> findByUbigeoInei(Ubigeo ubigeo);
    Optional<District> findByUbigeoReniec(Ubigeo ubigeo);
}
