package FarmaciaERP.presentation.controllers;

import FarmaciaERP.application.dto.Request.CrearUsuarioResquest;
import FarmaciaERP.application.dto.Request.LoginRequest;
import FarmaciaERP.application.dto.Response.CrearUsuarioResponse;
import FarmaciaERP.application.usecases.usuario.CrearUsuarioUseCase;
import FarmaciaERP.application.usecases.auth.LoginUsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final CrearUsuarioUseCase crearUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            String ip = obtenerIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            return ResponseEntity.ok(loginUsuarioUseCase.execute(request, ip, userAgent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CrearUsuarioResquest request){
        try {
            CrearUsuarioResponse response = crearUsuarioUseCase.ejecutar(request);
            if(response.getUsername().equals("Error")){
                return ResponseEntity.badRequest().body(response.getMensaje());
            }return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String obtenerIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}