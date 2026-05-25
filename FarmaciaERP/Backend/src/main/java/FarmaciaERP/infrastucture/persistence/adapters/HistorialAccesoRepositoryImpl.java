package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.LoginHistory;
import FarmaciaERP.domain.repositories.IHistorialAccesoRepository;
import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import FarmaciaERP.infrastucture.persistence.mappers.LoginHistoryMapper;
import FarmaciaERP.infrastucture.persistence.repositories.ILoginHistoryJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistorialAccesoRepositoryImpl implements IHistorialAccesoRepository {
    private final ILoginHistoryJPARepository historialAccesoJPARepository;

    public HistorialAccesoRepositoryImpl(ILoginHistoryJPARepository historialAccesoJPARepository) {
        this.historialAccesoJPARepository = historialAccesoJPARepository;
    }

    @Override
    public LoginHistory save(LoginHistory historial) {
        LoginHistoryJPA saved = LoginHistoryMapper.toEntity(historial);
        historialAccesoJPARepository.save(saved);
        return LoginHistoryMapper.toDomain(saved);
    }

    @Override
    public List<LoginHistory> findAll() {
        return historialAccesoJPARepository.findAll()
                .stream()
                .map(LoginHistoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<LoginHistory> findByUsuarioId(Long usuarioId) {
        return historialAccesoJPARepository.findByUsuario_Id(usuarioId)
                .stream()
                .map(LoginHistoryMapper::toDomain)
                .toList();
    }
}
