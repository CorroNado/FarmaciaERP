package FarmaciaERP.application.usecases.auth;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class VerificarEstadoUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public void execute(User user) {
        if (user.getEstado() == UserStatus.INACTIVO) {
            throw new RuntimeException("Cuenta fuera de servicio");
        }

        if (user.getEstado() == UserStatus.BLOQUEADO) {
            if (user.getLockUntil() == null) {
                throw new RuntimeException("Cuenta bloqueada. Contacta al administrador");
            }
            if (user.getLockUntil().isAfter(LocalDateTime.now())) {
                long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockUntil());
                throw new RuntimeException("Cuenta bloqueada. Intenta en " + minutesLeft + " minutos");
            }

            user.setEstado(UserStatus.ACTIVO);
            user.setLockUntil(null);
            user.setLoginAttempts(0);
            usuarioRepository.save(user);
        }
    }
}
