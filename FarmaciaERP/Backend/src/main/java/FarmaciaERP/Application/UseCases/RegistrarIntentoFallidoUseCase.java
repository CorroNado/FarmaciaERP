package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.ResultadoBloqueo;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class RegistrarIntentoFallidoUseCase {

    private final IUsuarioRepository usuarioRepository;
    public ResultadoBloqueo execute(Usuario usuario, int maxAttempts, int lockMinutes) {
        int attempts = usuario.getLoginAttempts() + 1;

        if (attempts >= maxAttempts) {
            usuario.setLockUntil(LocalDateTime.now().plusMinutes(lockMinutes));
            usuario.setLoginAttempts(0);
            usuario.setEstado(UsuarioEstados.BLOQUEADO);
            usuarioRepository.save(usuario);
            return ResultadoBloqueo.CUENTA_BLOQUEADA;
        }

        usuario.setLoginAttempts(attempts);
        usuarioRepository.save(usuario);
        return ResultadoBloqueo.INTENTO_REGISTRADO;
    }
}
