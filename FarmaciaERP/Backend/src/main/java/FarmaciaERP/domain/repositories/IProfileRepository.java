package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Profile;

import java.util.List;
import java.util.Optional;

public interface IProfileRepository {
    Optional<Profile> findById(Long id);
    List<Profile> findAll();
    Optional<Profile> findByName(String name);
}
