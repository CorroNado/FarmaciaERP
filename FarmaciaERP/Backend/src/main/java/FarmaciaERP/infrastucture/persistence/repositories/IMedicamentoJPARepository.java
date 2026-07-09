package FarmaciaERP.infrastucture.persistence.repositories;

import FarmaciaERP.domain.enums.MedicamentoCategoria;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IMedicamentoJPARepository extends JpaRepository<MedicamentoJPA, Integer> {

    List<MedicamentoJPA> findByNombreContainingIgnoreCase(String nombre);

    List<MedicamentoJPA> findByCategoria(MedicamentoCategoria categoria);

    List<MedicamentoJPA> findByFechaVencimientoBefore(LocalDate fecha);

    List<MedicamentoJPA> findByPresentacion(String presentacion);
}
