package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.Profile;
import FarmaciaERP.domain.repositories.IProfileRepository;
import FarmaciaERP.infrastucture.persistence.mappers.ProfileMapper;
import FarmaciaERP.infrastucture.persistence.repositories.IProfileJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements IProfileRepository {
    private final IProfileJPARepository jpaRepository;
    private final ProfileMapper profileMapper;

    @Override
    public Optional<Profile> findById(Long id) {
        return jpaRepository.findById(id)
                .map(profileMapper::ToDomain);
    }

    @Override
    public List<Profile> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(profileMapper::ToDomain)
                .toList();
    }

    @Override
    public Optional<Profile> findByName(String name) {
        return jpaRepository.findByNombre(name)
                .map(profileMapper::ToDomain);
    }
}
