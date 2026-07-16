package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.AprobarLoteRequest;
import FarmaciaERP.Application.DTOs.Request.RechazarLoteRequest;
import FarmaciaERP.Application.DTOs.Response.InspeccionCalidadResponse;
import FarmaciaERP.Application.UseCases.AprobarLoteUseCase;
import FarmaciaERP.Application.UseCases.BuscarInspeccionCalidadUseCase;
import FarmaciaERP.Application.UseCases.RechazarLoteUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.06 - Fase 05: inspección y aseguramiento de calidad (QA11).
 */
@RestController
@RequestMapping("/api/logistica/inspecciones-calidad")
public class InspeccionCalidadController {

    private final AprobarLoteUseCase aprobarLoteUseCase;
    private final RechazarLoteUseCase rechazarLoteUseCase;
    private final BuscarInspeccionCalidadUseCase buscarInspeccionCalidadUseCase;

    public InspeccionCalidadController(AprobarLoteUseCase aprobarLoteUseCase,
                                        RechazarLoteUseCase rechazarLoteUseCase,
                                        BuscarInspeccionCalidadUseCase buscarInspeccionCalidadUseCase) {
        this.aprobarLoteUseCase = aprobarLoteUseCase;
        this.rechazarLoteUseCase = rechazarLoteUseCase;
        this.buscarInspeccionCalidadUseCase = buscarInspeccionCalidadUseCase;
    }

    @PostMapping("/aprobar")
    public ResponseEntity<?> aprobar(@RequestBody AprobarLoteRequest request) {
        try {
            InspeccionCalidadResponse aprobada = aprobarLoteUseCase.ejecutar(request);
            return new ResponseEntity<>(aprobada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/rechazar")
    public ResponseEntity<?> rechazar(@RequestBody RechazarLoteRequest request) {
        try {
            InspeccionCalidadResponse rechazada = rechazarLoteUseCase.ejecutar(request);
            return new ResponseEntity<>(rechazada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspeccionCalidadResponse> obtenerPorId(@PathVariable Long id) {
        return buscarInspeccionCalidadUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<InspeccionCalidadResponse>> obtenerTodas(
            @RequestParam(required = false) Long entradaMercanciaId) {
        if (entradaMercanciaId != null) {
            return ResponseEntity.ok(buscarInspeccionCalidadUseCase.porEntradaMercancia(entradaMercanciaId));
        }
        return ResponseEntity.ok(buscarInspeccionCalidadUseCase.todas());
    }
}
