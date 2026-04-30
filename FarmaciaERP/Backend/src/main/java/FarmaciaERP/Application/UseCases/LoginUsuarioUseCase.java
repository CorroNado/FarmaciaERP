package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Application.Security.CustomUserDetails;
import FarmaciaERP.Application.Security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class LoginUsuarioUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RegistrarHistorialAccesoUseCase historialUseCase;

    public LoginUsuarioUseCase(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RegistrarHistorialAccesoUseCase historialUseCase) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.historialUseCase = historialUseCase;

    }

    public LoginResponse execute(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            String token = jwtUtils.generateToken(
                    user.getId(),
                    user.getUsername()
            );

            historialUseCase.ejecutar(
                    user.getId(),
                    user.getUsername(),
                    "LOGIN",
                    "127.0.0.1"
            );
            return new LoginResponse(token);

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}