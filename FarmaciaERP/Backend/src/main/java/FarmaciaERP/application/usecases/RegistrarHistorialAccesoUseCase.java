package FarmaciaERP.application.usecases;

import FarmaciaERP.domain.entities.LoginHistory;
import FarmaciaERP.domain.enums.LoginAction;
import FarmaciaERP.domain.repositories.ILoginHistoryRepository;

import org.springframework.stereotype.Service;

@Service
public class RegistrarHistorialAccesoUseCase {

    private final ILoginHistoryRepository historialRepository;

    public RegistrarHistorialAccesoUseCase(ILoginHistoryRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public LoginHistory execute(Long usuarioId, LoginAction accion, String ip, String userAgent) {

        LoginHistory historial = new LoginHistory(usuarioId, accion, ip, userAgent);
        historialRepository.save(historial);

        return historial;
    }

}