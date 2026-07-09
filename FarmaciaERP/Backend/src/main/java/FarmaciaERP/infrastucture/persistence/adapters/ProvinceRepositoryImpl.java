package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Province;
import FarmaciaERP.domain.repositories.IProvinceRepository;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.mappers.ProvinceMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IProvinceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor

public class ProvinceRepositoryImpl implements IProvinceRepository {
    private final IProvinceJPARepository jpaRepository;
    private final ProvinceMapper provinceMapper;

    @Override
    public List<Province> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(provinceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Province> findById(Long id) {
        return jpaRepository.findById(id)
                .map(provinceMapper::toDomain);
    }

    @Override
    public Optional<Province> findByUbigeoInei(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoInei(ubigeo.getUbigeoInei())
                .map(provinceMapper::toDomain);
    }

    @Override
    public Optional<Province> findByUbigeoReniec(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoReniec(ubigeo.getUbigeoReniec())
                .map(provinceMapper::toDomain);
    }

}
