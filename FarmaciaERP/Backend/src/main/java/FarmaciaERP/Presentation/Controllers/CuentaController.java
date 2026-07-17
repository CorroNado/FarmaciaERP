package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearCuentaRequest;
import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Application.UseCases.BuscarCuentaUseCase;
import FarmaciaERP.Application.UseCases.CrearCuentaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/cuentas")
public class CuentaController {

    private final CrearCuentaUseCase crearCuentaUseCase;
    private final BuscarCuentaUseCase buscarCuentaUseCase;

    public CuentaController(CrearCuentaUseCase crearCuentaUseCase, BuscarCuentaUseCase buscarCuentaUseCase) {
        this.crearCuentaUseCase = crearCuentaUseCase;
        this.buscarCuentaUseCase = buscarCuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCuentaRequest request) {
        try {
            CuentaResponse creada = crearCuentaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCuentaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> obtenerTodas(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        return ResponseEntity.ok(soloActivas ? buscarCuentaUseCase.activas() : buscarCuentaUseCase.todas());
    }
}