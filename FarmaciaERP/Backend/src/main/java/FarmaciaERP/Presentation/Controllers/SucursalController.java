package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearSucursalRequest;
import FarmaciaERP.Application.DTOs.Response.SucursalResponse;
import FarmaciaERP.Application.UseCases.BuscarSucursalUseCase;
import FarmaciaERP.Application.UseCases.CrearSucursalUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.07 - Maestro de sucursales destino para la distribución capilar
 * (Fase 06).
 */
@RestController
@RequestMapping("/api/logistica/sucursales")
public class SucursalController {

    private final CrearSucursalUseCase crearSucursalUseCase;
    private final BuscarSucursalUseCase buscarSucursalUseCase;

    public SucursalController(CrearSucursalUseCase crearSucursalUseCase,
                               BuscarSucursalUseCase buscarSucursalUseCase) {
        this.crearSucursalUseCase = crearSucursalUseCase;
        this.buscarSucursalUseCase = buscarSucursalUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearSucursalRequest request) {
        try {
            SucursalResponse creada = crearSucursalUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> obtenerPorId(@PathVariable Long id) {
        return buscarSucursalUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> obtenerTodas(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        return ResponseEntity.ok(soloActivas ? buscarSucursalUseCase.activas() : buscarSucursalUseCase.todas());
    }
}
