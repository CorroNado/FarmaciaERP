package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.EjecutarConciliacionTresViasRequest;
import FarmaciaERP.Application.DTOs.Response.ConciliacionTresViasResponse;
import FarmaciaERP.Application.UseCases.BuscarConciliacionTresViasUseCase;
import FarmaciaERP.Application.UseCases.EjecutarConciliacionTresViasUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.08 - Fase 08: conciliación de 3 vías (3-Way Match / MRBR).
 */
@RestController
@RequestMapping("/api/logistica/conciliaciones-tres-vias")
public class ConciliacionTresViasController {

    private final EjecutarConciliacionTresViasUseCase ejecutarConciliacionTresViasUseCase;
    private final BuscarConciliacionTresViasUseCase buscarConciliacionTresViasUseCase;

    public ConciliacionTresViasController(EjecutarConciliacionTresViasUseCase ejecutarConciliacionTresViasUseCase,
                                           BuscarConciliacionTresViasUseCase buscarConciliacionTresViasUseCase) {
        this.ejecutarConciliacionTresViasUseCase = ejecutarConciliacionTresViasUseCase;
        this.buscarConciliacionTresViasUseCase = buscarConciliacionTresViasUseCase;
    }

    @PostMapping
    public ResponseEntity<?> ejecutar(@RequestBody EjecutarConciliacionTresViasRequest request) {
        try {
            ConciliacionTresViasResponse resultado = ejecutarConciliacionTresViasUseCase.ejecutar(request);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConciliacionTresViasResponse> obtenerPorId(@PathVariable Long id) {
        return buscarConciliacionTresViasUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ConciliacionTresViasResponse>> obtenerTodas(
            @RequestParam(required = false) Long ordenCompraId) {
        if (ordenCompraId != null) {
            return ResponseEntity.ok(buscarConciliacionTresViasUseCase.porOrdenCompra(ordenCompraId));
        }
        return ResponseEntity.ok(buscarConciliacionTresViasUseCase.todas());
    }
}
