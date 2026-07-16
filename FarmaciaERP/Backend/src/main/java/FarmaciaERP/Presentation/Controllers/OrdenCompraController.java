package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearOrdenCompraRequest;
import FarmaciaERP.Application.DTOs.Request.FirmarOrdenCompraRequest;
import FarmaciaERP.Application.DTOs.Response.OrdenCompraResponse;
import FarmaciaERP.Application.UseCases.BuscarOrdenCompraUseCase;
import FarmaciaERP.Application.UseCases.CrearOrdenCompraUseCase;
import FarmaciaERP.Application.UseCases.FirmarOrdenCompraUseCase;
import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.03 - Fase 03: tratamiento y emisión de la Orden de Compra (ME21N).
 */
@RestController
@RequestMapping("/api/logistica/ordenes-compra")
public class OrdenCompraController {

    private final CrearOrdenCompraUseCase crearOrdenCompraUseCase;
    private final FirmarOrdenCompraUseCase firmarOrdenCompraUseCase;
    private final BuscarOrdenCompraUseCase buscarOrdenCompraUseCase;

    public OrdenCompraController(CrearOrdenCompraUseCase crearOrdenCompraUseCase,
                                  FirmarOrdenCompraUseCase firmarOrdenCompraUseCase,
                                  BuscarOrdenCompraUseCase buscarOrdenCompraUseCase) {
        this.crearOrdenCompraUseCase = crearOrdenCompraUseCase;
        this.firmarOrdenCompraUseCase = firmarOrdenCompraUseCase;
        this.buscarOrdenCompraUseCase = buscarOrdenCompraUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearOrdenCompraRequest request) {
        try {
            OrdenCompraResponse creada = crearOrdenCompraUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/firmar")
    public ResponseEntity<?> firmar(@PathVariable Long id, @RequestBody FirmarOrdenCompraRequest request) {
        try {
            return ResponseEntity.ok(firmarOrdenCompraUseCase.ejecutar(id, request.getFechaEntregaLimite()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraResponse> obtenerPorId(@PathVariable Long id) {
        return buscarOrdenCompraUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrdenCompraResponse>> obtenerTodas(
            @RequestParam(required = false) Long solPedId,
            @RequestParam(required = false) EstadoOrdenCompra estado) {
        if (solPedId != null) {
            return ResponseEntity.ok(buscarOrdenCompraUseCase.porSolPed(solPedId));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarOrdenCompraUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarOrdenCompraUseCase.todas());
    }
}
