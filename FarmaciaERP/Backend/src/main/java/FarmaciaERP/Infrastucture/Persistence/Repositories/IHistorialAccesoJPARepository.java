package FarmaciaERP.Infrastucture.Persistence.Repositories;

import FarmaciaERP.Infrastucture.Persistence.Entities.HistorialAccesoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IHistorialAccesoJPARepository  extends JpaRepository<HistorialAccesoJPA, Long> {
    List<HistorialAccesoJPA> findByUsuario_Id(Long usuarioId);
}
