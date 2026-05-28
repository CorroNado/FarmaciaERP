package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Application.UseCases.LoginUsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import FarmaciaERP.Application.DTOs.Request.CrearUsuarioResquest;
import FarmaciaERP.Application.UseCases.CrearUsuarioUseCase;

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
    public ResponseEntity<?> register(@RequestBody CrearUsuarioResquest request){
        return ResponseEntity.ok(registerUsuarioUseCase.ejecutar(request));
    }

    private String obtenerIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}