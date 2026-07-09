package FarmaciaERP.application.usecases.auth;

import FarmaciaERP.domain.entities.User;
import FarmaciaERP.domain.enums.ResultadoBloqueo;
import FarmaciaERP.domain.enums.UserStatus;
import FarmaciaERP.domain.repositories.IUsuarioRepository;
import FarmaciaERP.domain.valueObjects.usuario.LoginSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RegistrarIntentoFallidoUseCase {

    private final IUsuarioRepository usuarioRepository;
    public ResultadoBloqueo execute(User user, int maxAttempts, int lockMinutes) {
        int attempts = user.getLoginSeguro().getIntentosLogin() + 1;

        LoginSecurity loginSecurity;

        if (attempts >= maxAttempts) {
            loginSecurity = new LoginSecurity(
                    attempts,
                    LocalDateTime.now().plusMinutes(lockMinutes),
                    UserStatus.BLOQUEADO
            );
            user.setLoginSeguro(loginSecurity);
            usuarioRepository.save(user);
            return ResultadoBloqueo.CUENTA_BLOQUEADA;
        }
        loginSecurity = new LoginSecurity(attempts);
        user.setLoginSeguro(loginSecurity);
        usuarioRepository.save(user);
        return ResultadoBloqueo.INTENTO_REGISTRADO;
    }
}
