package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.PagoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPagoJPARepository extends JpaRepository<PagoJPA, Long> {

    List<PagoJPA> findByFacturaMIRO_Id(Long facturaMIROId);

    boolean existsByFacturaMIRO_Id(Long facturaMIROId);
}
