package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoProveedor;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProveedorJPARepository extends JpaRepository<ProveedorJPA, Long> {

    Optional<ProveedorJPA> findByRuc(String ruc);

    List<ProveedorJPA> findByRazonSocialContainingIgnoreCase(String razonSocial);

    List<ProveedorJPA> findByEstado(EstadoProveedor estado);
}
