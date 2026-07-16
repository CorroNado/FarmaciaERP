package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.RegistrarEntradaMercanciaRequest;
import FarmaciaERP.Application.DTOs.Response.EntradaMercanciaResponse;
import FarmaciaERP.Application.UseCases.BuscarEntradaMercanciaUseCase;
import FarmaciaERP.Application.UseCases.RegistrarEntradaMercanciaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.05 - Fase 04: entrada de mercancía y registro (MIGO).
 */
@RestController
@RequestMapping("/api/logistica/entradas-mercancia")
public class EntradaMercanciaController {

    private final RegistrarEntradaMercanciaUseCase registrarEntradaMercanciaUseCase;
    private final BuscarEntradaMercanciaUseCase buscarEntradaMercanciaUseCase;

    public EntradaMercanciaController(RegistrarEntradaMercanciaUseCase registrarEntradaMercanciaUseCase,
                                       BuscarEntradaMercanciaUseCase buscarEntradaMercanciaUseCase) {
        this.registrarEntradaMercanciaUseCase = registrarEntradaMercanciaUseCase;
        this.buscarEntradaMercanciaUseCase = buscarEntradaMercanciaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistrarEntradaMercanciaRequest request) {
        try {
            EntradaMercanciaResponse registrada = registrarEntradaMercanciaUseCase.ejecutar(request);
            return new ResponseEntity<>(registrada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaMercanciaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarEntradaMercanciaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EntradaMercanciaResponse>> obtenerTodas(
            @RequestParam(required = false) Long ordenCompraId) {
        if (ordenCompraId != null) {
            return ResponseEntity.ok(buscarEntradaMercanciaUseCase.porOrdenCompra(ordenCompraId));
        }
        return ResponseEntity.ok(buscarEntradaMercanciaUseCase.todas());
    }
}
