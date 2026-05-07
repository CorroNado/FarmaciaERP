package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Domain.Entities.HistorialAcceso;
import FarmaciaERP.Domain.Enums.AccionAcceso;
import FarmaciaERP.Domain.Enums.ResultadoBloqueo;
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

@Component
@RequiredArgsConstructor
public class LoginUsuarioUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUsuarioRepository usuarioRepository;

    private final VerificarEstadoUsuarioUseCase verificarEstadoUsuarioUseCase;
    private final RegistrarIntentoFallidoUseCase registrarIntentoFallidoUseCase;

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_MINUTES = 1;
    private final RegistrarHistorialAccesoUseCase registrarHistorialAccesoUseCase;


    public LoginResponse execute(LoginRequest request,String ip, String userAgent) {
        var usuario = usuarioRepository.findByEmail(new Email(request.getEmail()))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        verificarEstadoUsuarioUseCase.execute(usuario);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            usuario.setLoginAttempts(0);
            usuario.setLockUntil(null);
            registrarHistorialAccesoUseCase.execute(usuario.getId(),AccionAcceso.LOGIN_EXITOSO,ip,userAgent);
            usuarioRepository.save(usuario);

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtUtils.generateToken(
                    user.getId(),
                    user.getUsername()
            );

            return new LoginResponse(token);

        } catch (BadCredentialsException ex) {
            ResultadoBloqueo resultado = registrarIntentoFallidoUseCase.execute(usuario, MAX_ATTEMPTS, LOCK_MINUTES);

            if (resultado == ResultadoBloqueo.CUENTA_BLOQUEADA) {
                registrarHistorialAccesoUseCase.execute(usuario.getId(), AccionAcceso.CUENTA_BLOQUEADA, ip, userAgent);
                throw new RuntimeException("Cuenta bloqueada por " + LOCK_MINUTES + " minutos");
            }

            registrarHistorialAccesoUseCase.execute(usuario.getId(), AccionAcceso.LOGIN_FALLIDO, ip, userAgent);
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}