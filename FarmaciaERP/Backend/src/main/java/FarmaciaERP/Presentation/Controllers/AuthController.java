package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.LoginRequest;
import FarmaciaERP.Application.DTOs.Request.RegisterRequest;
import FarmaciaERP.Application.DTOs.Response.LoginResponse;
import FarmaciaERP.Application.DTOs.Response.RegisterResponse;
import FarmaciaERP.Application.UseCases.LoginUsuarioUseCase;
import FarmaciaERP.Application.UseCases.RegisterUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final RegisterUsuarioUseCase registerUsuarioUseCase;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginUsuarioUseCase.execute(request);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request){
        return registerUsuarioUseCase.execute(request);
    }
}