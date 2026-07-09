package FarmaciaERP.infrastucture.persistence.adapters;

import FarmaciaERP.domain.entities.LoginHistory;
import FarmaciaERP.domain.repositories.ILoginHistoryRepository;
import FarmaciaERP.infrastucture.persistence.entities.LoginHistoryJPA;
import FarmaciaERP.infrastucture.persistence.mappers.LoginHistoryMapper;
import FarmaciaERP.infrastucture.persistence.repositories.ILoginHistoryJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LoginHistoryRepositoryImpl implements ILoginHistoryRepository {
    private final ILoginHistoryJPARepository historialAccesoJPARepository;
    private final LoginHistoryMapper loginHistoryMapper;


    @Override
    public LoginHistory save(LoginHistory historial) {
        LoginHistoryJPA saved = loginHistoryMapper.toEntity(historial);
        historialAccesoJPARepository.save(saved);
        return loginHistoryMapper.toDomain(saved);
    }

    @Override
    public List<LoginHistory> findAll() {
        return historialAccesoJPARepository.findAll()
                .stream()
                .map(loginHistoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<LoginHistory> findByUsuarioId(Long usuarioId) {
        return historialAccesoJPARepository.findByUser(usuarioId)
                .stream()
                .map(loginHistoryMapper::toDomain)
                .toList();
    }
}
