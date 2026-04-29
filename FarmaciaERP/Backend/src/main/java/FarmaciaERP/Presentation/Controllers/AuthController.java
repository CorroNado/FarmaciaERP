package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Application.UseCases.LoginUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUsuarioUseCase loginUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUsuarioUseCase.execute(request));
    }
}