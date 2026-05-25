package FarmaciaERP.domain.repositories;

import FarmaciaERP.domain.entities.Profile;

import java.util.List;
import java.util.Optional;

public interface IProfileRepository {
    Profile save(Profile profile);
    Optional<Profile> findById(Long id);
    List<Profile> findAll();
    Optional<Profile> findByName(String name);
}
