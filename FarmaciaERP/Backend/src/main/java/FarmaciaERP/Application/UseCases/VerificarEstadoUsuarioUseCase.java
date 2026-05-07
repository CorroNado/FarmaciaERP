package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class VerificarEstadoUsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;

    public void execute(Usuario usuario) {
        if (usuario.getEstado() == UsuarioEstados.INACTIVO) {
            throw new RuntimeException("Cuenta fuera de servicio");
        }

        if (usuario.getEstado() == UsuarioEstados.BLOQUEADO) {
            if (usuario.getLockUntil() == null) {
                throw new RuntimeException("Cuenta bloqueada. Contacta al administrador");
            }
            if (usuario.getLockUntil().isAfter(LocalDateTime.now())) {
                long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), usuario.getLockUntil());
                throw new RuntimeException("Cuenta bloqueada. Intenta en " + minutesLeft + " minutos");
            }

            usuario.setEstado(UsuarioEstados.ACTIVO);
            usuario.setLockUntil(null);
            usuario.setLoginAttempts(0);
            usuarioRepository.save(usuario);
        }
    }
}
