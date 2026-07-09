package FarmaciaERP.presentation.controllers;

import FarmaciaERP.application.dto.Request.CrearUsuarioRequest;
import FarmaciaERP.application.dto.Request.LoginRequest;
import FarmaciaERP.application.dto.Response.CrearUsuarioResponse;
import FarmaciaERP.application.dto.Response.LoginResponse;
import FarmaciaERP.application.usecases.auth.LoginUsuarioUseCase;
import FarmaciaERP.application.usecases.usuario.CrearUsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final CrearUsuarioUseCase registerUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest) {
        String ip = obtenerIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        return ResponseEntity.ok(loginUsuarioUseCase.execute(request, ip, userAgent));
    }



    @PostMapping("/register")
    public CrearUsuarioResponse register(@RequestBody CrearUsuarioRequest request){
        return registerUsuarioUseCase.ejecutar(request);
    }

    private String obtenerIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}