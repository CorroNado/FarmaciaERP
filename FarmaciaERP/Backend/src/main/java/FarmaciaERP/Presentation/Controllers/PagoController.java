package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.EjecutarPagoRequest;
import FarmaciaERP.Application.DTOs.Response.PagoResponse;
import FarmaciaERP.Application.UseCases.BuscarPagoUseCase;
import FarmaciaERP.Application.UseCases.EjecutarPagoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.09 - Fase 09: gestión y ejecución del pago (F110).
 */
@RestController
@RequestMapping("/api/logistica/pagos")
public class PagoController {

    private final EjecutarPagoUseCase ejecutarPagoUseCase;
    private final BuscarPagoUseCase buscarPagoUseCase;

    public PagoController(EjecutarPagoUseCase ejecutarPagoUseCase, BuscarPagoUseCase buscarPagoUseCase) {
        this.ejecutarPagoUseCase = ejecutarPagoUseCase;
        this.buscarPagoUseCase = buscarPagoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> ejecutar(@RequestBody EjecutarPagoRequest request) {
        try {
            PagoResponse pago = ejecutarPagoUseCase.ejecutar(request);
            return new ResponseEntity<>(pago, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Long id) {
        return buscarPagoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> obtenerTodos(
            @RequestParam(required = false) Long facturaMIROId) {
        if (facturaMIROId != null) {
            return ResponseEntity.ok(buscarPagoUseCase.porFacturaMIRO(facturaMIROId));
        }
        return ResponseEntity.ok(buscarPagoUseCase.todos());
    }
}
