package FarmaciaERP.application.usecases.auth;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.usuario.LoginSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class VerificarEstadoUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public void execute(User user) {
        LoginSecurity loginSecurity = user.getLoginSeguro();
        if (loginSecurity.getEstado() == UserStatus.INACTIVO) {
            throw new RuntimeException("Cuenta fuera de servicio");
        }

        if (loginSecurity.getEstado() == UserStatus.BLOQUEADO) {
            if (loginSecurity.getTiempoDesbloqueo() == null) {
                throw new RuntimeException("Cuenta bloqueada. Contacta al administrador");
            }
            if (loginSecurity.getTiempoDesbloqueo().isAfter(LocalDateTime.now())) {
                long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), loginSecurity.getTiempoDesbloqueo());
                throw new RuntimeException("Cuenta bloqueada. Intenta en " + minutesLeft + " minutos");
            }

            user.setLoginSeguro(new LoginSecurity());
            usuarioRepository.save(user);
        }
    }
}
