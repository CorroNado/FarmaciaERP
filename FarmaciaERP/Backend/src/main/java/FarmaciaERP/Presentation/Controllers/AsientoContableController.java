package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearAsientoContableRequest;
import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Application.UseCases.BuscarAsientoContableUseCase;
import FarmaciaERP.Application.UseCases.ContabilizarAsientoUseCase;
import FarmaciaERP.Application.UseCases.CrearAsientoContableUseCase;
import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/asientos")
public class AsientoContableController {

    private final CrearAsientoContableUseCase crearAsientoUseCase;
    private final BuscarAsientoContableUseCase buscarAsientoUseCase;
    private final ContabilizarAsientoUseCase contabilizarAsientoUseCase;

    public AsientoContableController(CrearAsientoContableUseCase crearAsientoUseCase,
                                     BuscarAsientoContableUseCase buscarAsientoUseCase,
                                     ContabilizarAsientoUseCase contabilizarAsientoUseCase) {
        this.crearAsientoUseCase = crearAsientoUseCase;
        this.buscarAsientoUseCase = buscarAsientoUseCase;
        this.contabilizarAsientoUseCase = contabilizarAsientoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearAsientoContableRequest request) {
        try {
            AsientoContableResponse creado = crearAsientoUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsientoContableResponse> obtenerPorId(@PathVariable Long id) {
        return buscarAsientoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AsientoContableResponse>> obtenerTodos(
            @RequestParam(required = false) EstadoAsiento estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return ResponseEntity.ok(buscarAsientoUseCase.porRangoFechas(fechaInicio, fechaFin));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarAsientoUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarAsientoUseCase.todos());
    }

    @PostMapping("/{id}/contabilizar")
    public ResponseEntity<?> contabilizar(@PathVariable Long id) {
        try {
            AsientoContableResponse contabilizado = contabilizarAsientoUseCase.ejecutar(id);
            return ResponseEntity.ok(contabilizado);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}