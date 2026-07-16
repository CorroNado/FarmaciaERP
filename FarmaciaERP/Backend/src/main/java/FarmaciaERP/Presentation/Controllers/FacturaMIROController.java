package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.RegistrarFacturaMIRORequest;
import FarmaciaERP.Application.DTOs.Response.FacturaMIROResponse;
import FarmaciaERP.Application.UseCases.BuscarFacturaMIROUseCase;
import FarmaciaERP.Application.UseCases.RegistrarFacturaMIROUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.07 - Fase 07: verificación de factura del proveedor (MIRO).
 */
@RestController
@RequestMapping("/api/logistica/facturas-miro")
public class FacturaMIROController {

    private final RegistrarFacturaMIROUseCase registrarFacturaMIROUseCase;
    private final BuscarFacturaMIROUseCase buscarFacturaMIROUseCase;

    public FacturaMIROController(RegistrarFacturaMIROUseCase registrarFacturaMIROUseCase,
                                  BuscarFacturaMIROUseCase buscarFacturaMIROUseCase) {
        this.registrarFacturaMIROUseCase = registrarFacturaMIROUseCase;
        this.buscarFacturaMIROUseCase = buscarFacturaMIROUseCase;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistrarFacturaMIRORequest request) {
        try {
            FacturaMIROResponse registrada = registrarFacturaMIROUseCase.ejecutar(request);
            return new ResponseEntity<>(registrada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaMIROResponse> obtenerPorId(@PathVariable Long id) {
        return buscarFacturaMIROUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FacturaMIROResponse>> obtenerTodas(
            @RequestParam(required = false) Long ordenCompraId) {
        if (ordenCompraId != null) {
            return ResponseEntity.ok(buscarFacturaMIROUseCase.porOrdenCompra(ordenCompraId));
        }
        return ResponseEntity.ok(buscarFacturaMIROUseCase.todas());
    }
}
