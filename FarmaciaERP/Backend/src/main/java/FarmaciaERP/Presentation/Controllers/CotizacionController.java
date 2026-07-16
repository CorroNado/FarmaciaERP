package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearCotizacionRequest;
import FarmaciaERP.Application.DTOs.Request.RechazarCotizacionRequest;
import FarmaciaERP.Application.DTOs.Response.AceptarCotizacionResponse;
import FarmaciaERP.Application.DTOs.Response.CotizacionResponse;
import FarmaciaERP.Application.UseCases.AceptarCotizacionUseCase;
import FarmaciaERP.Application.UseCases.BuscarCotizacionUseCase;
import FarmaciaERP.Application.UseCases.CrearCotizacionUseCase;
import FarmaciaERP.Application.UseCases.RechazarCotizacionUseCase;
import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Exceptions.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizacion")
public class CotizacionController {

    private final CrearCotizacionUseCase crearCotizacionUseCase;
    private final AceptarCotizacionUseCase aceptarCotizacionUseCase;
    private final RechazarCotizacionUseCase rechazarCotizacionUseCase;
    private final BuscarCotizacionUseCase buscarCotizacionUseCase;

    public CotizacionController(CrearCotizacionUseCase crearCotizacionUseCase,
                                 AceptarCotizacionUseCase aceptarCotizacionUseCase,
                                 RechazarCotizacionUseCase rechazarCotizacionUseCase,
                                 BuscarCotizacionUseCase buscarCotizacionUseCase) {
        this.crearCotizacionUseCase = crearCotizacionUseCase;
        this.aceptarCotizacionUseCase = aceptarCotizacionUseCase;
        this.rechazarCotizacionUseCase = rechazarCotizacionUseCase;
        this.buscarCotizacionUseCase = buscarCotizacionUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCotizacionRequest request) {
        try {
            CotizacionResponse cotizacion = crearCotizacionUseCase.ejecutar(request);
            return new ResponseEntity<>(cotizacion, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptar(@PathVariable Long id) {
        try {
            AceptarCotizacionResponse response = aceptarCotizacionUseCase.ejecutar(id);
            return ResponseEntity.ok(response);
        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id, @RequestBody RechazarCotizacionRequest request) {
        try {
            return ResponseEntity.ok(rechazarCotizacionUseCase.ejecutar(id, request.getMotivo()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCotizacionUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CotizacionResponse>> obtenerTodas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) EstadoCotizacion estado) {

        if (clienteId != null) {
            return ResponseEntity.ok(buscarCotizacionUseCase.porCliente(clienteId));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarCotizacionUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarCotizacionUseCase.todas());
    }
}
