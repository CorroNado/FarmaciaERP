package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Infrastucture.Persistence.Entities.AsientoContableJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAsientoContableJPARepository extends JpaRepository<AsientoContableJPA, Long> {

    Optional<AsientoContableJPA> findByNumero(String numero);

    List<AsientoContableJPA> findByEstado(EstadoAsiento estado);

    List<AsientoContableJPA> findByFechaContableBetween(LocalDate inicio, LocalDate fin);

    boolean existsByNumero(String numero);
}