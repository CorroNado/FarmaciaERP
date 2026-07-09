package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.District;
import FarmaciaERP.domain.repositories.IDistrictRepository;
import FarmaciaERP.domain.valueObjects.Ubigeo;
import FarmaciaERP.infrastucture.persistence.mappers.DistrictMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IDistrictJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DistrictRepositoryImpl implements IDistrictRepository {
    private final IDistrictJPARepository jpaRepository;
    private final DistrictMapper districtMapper;

    @Override
    public List<District> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(districtMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<District> findById(Long id) {
        return jpaRepository.findById(id)
                .map(districtMapper::toDomain);
    }

    @Override
    public Optional<District> findByUbigeoInei(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoInei(ubigeo.getUbigeoInei())
                .map(districtMapper::toDomain);
    }

    @Override
    public Optional<District> findByUbigeoReniec(Ubigeo ubigeo) {
        return jpaRepository.findByUbigeoReniec(ubigeo.getUbigeoReniec())
                .map(districtMapper::toDomain);
    }
}
