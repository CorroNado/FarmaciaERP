package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Infrastucture.Security.CustomUserDetails;
import FarmaciaERP.Infrastucture.Security.jwt.JwtUtils;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.Repositories.IUsuarioRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class LoginUsuarioUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUsuarioRepository usuarioRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_MINUTES = 1;


    public LoginResponse execute(LoginRequest request) {
        var usuario = usuarioRepository.findByEmail(new Email(request.getEmail()))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if(usuario.getEstado() == UsuarioEstados.INACTIVO){
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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            usuario.setLoginAttempts(0);
            usuario.setLockUntil(null);
            usuarioRepository.save(usuario);

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtUtils.generateToken(
                    user.getId(),
                    user.getUsername()
            );

            return new LoginResponse(token);

        } catch (BadCredentialsException ex) {
            Integer attempts = usuario.getLoginAttempts() + 1;

            if (attempts >= MAX_ATTEMPTS) {
                usuario.setLockUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
                usuario.setLoginAttempts(0);
                usuario.setEstado(UsuarioEstados.BLOQUEADO);
                usuarioRepository.save(usuario);
                throw new RuntimeException("Cuenta bloqueada por " + LOCK_MINUTES + " minutos");
            }

            usuario.setLoginAttempts(attempts);
            usuarioRepository.save(usuario);
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}