package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Domain.Enums.EstadoConvenio;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IConvenioJPARepository extends JpaRepository<ConvenioJPA, Long> {

    Optional<ConvenioJPA> findByNumero(String numero);

    List<ConvenioJPA> findByProveedor_Id(Long proveedorId);

    List<ConvenioJPA> findByEstado(EstadoConvenio estado);
}
