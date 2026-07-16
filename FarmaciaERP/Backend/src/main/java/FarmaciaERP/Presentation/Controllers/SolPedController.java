package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.*;
import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Application.DTOs.Response.SugerenciaMRPItemResponse;
import FarmaciaERP.Application.UseCases.*;
import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.01 / LOG.02 - Fases 01 y 02 del ciclo Procure-to-Pay: planificación de
 * necesidades (MRP) y determinación de la fuente de aprovisionamiento.
 */
@RestController
@RequestMapping("/api/logistica/solped")
public class SolPedController {

    private final GenerarSugerenciaMRPUseCase generarSugerenciaMRPUseCase;
    private final CrearSolPedUseCase crearSolPedUseCase;
    private final BuscarSolPedUseCase buscarSolPedUseCase;
    private final AprobarFuenteAprovisionamientoUseCase aprobarFuenteAprovisionamientoUseCase;
    private final RechazarSolPedUseCase rechazarSolPedUseCase;

    public SolPedController(GenerarSugerenciaMRPUseCase generarSugerenciaMRPUseCase,
                             CrearSolPedUseCase crearSolPedUseCase,
                             BuscarSolPedUseCase buscarSolPedUseCase,
                             AprobarFuenteAprovisionamientoUseCase aprobarFuenteAprovisionamientoUseCase,
                             RechazarSolPedUseCase rechazarSolPedUseCase) {
        this.generarSugerenciaMRPUseCase = generarSugerenciaMRPUseCase;
        this.crearSolPedUseCase = crearSolPedUseCase;
        this.buscarSolPedUseCase = buscarSolPedUseCase;
        this.aprobarFuenteAprovisionamientoUseCase = aprobarFuenteAprovisionamientoUseCase;
        this.rechazarSolPedUseCase = rechazarSolPedUseCase;
    }

    @PostMapping("/mrp")
    public ResponseEntity<?> calcularMRP(@RequestBody SugerenciaMRPRequest request) {
        try {
            List<SugerenciaMRPItemResponse> sugerencia = generarSugerenciaMRPUseCase.ejecutar(request);
            return ResponseEntity.ok(sugerencia);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearSolPedRequest request) {
        try {
            SolPedResponse creada = crearSolPedUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/aprobar-fuente")
    public ResponseEntity<?> aprobarFuente(@PathVariable Long id, @RequestBody AprobarFuenteRequest request) {
        try {
            return ResponseEntity.ok(aprobarFuenteAprovisionamientoUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id, @RequestBody RechazarSolPedRequest request) {
        try {
            return ResponseEntity.ok(rechazarSolPedUseCase.ejecutar(id, request.getMotivo()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolPedResponse> obtenerPorId(@PathVariable Long id) {
        return buscarSolPedUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SolPedResponse>> obtenerTodas(@RequestParam(required = false) EstadoSolPed estado) {
        if (estado != null) {
            return ResponseEntity.ok(buscarSolPedUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarSolPedUseCase.todas());
    }
}
