package FarmaciaERP.application.usecases.auth;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.ResultadoBloqueo;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RegistrarIntentoFallidoUseCase {

    private final IUsuarioRepository usuarioRepository;
    public ResultadoBloqueo execute(User user, int maxAttempts, int lockMinutes) {
        int attempts = user.getLoginAttempts() + 1;

        if (attempts >= maxAttempts) {
            user.setLockUntil(LocalDateTime.now().plusMinutes(lockMinutes));
            user.setLoginAttempts(0);
            user.setEstado(UserStatus.BLOQUEADO);
            usuarioRepository.save(user);
            return ResultadoBloqueo.CUENTA_BLOQUEADA;
        }

        user.setLoginAttempts(attempts);
        usuarioRepository.save(user);
        return ResultadoBloqueo.INTENTO_REGISTRADO;
    }
}
