package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.GenerarOrdenTrasladoRequest;
import FarmaciaERP.Application.DTOs.Response.OrdenTrasladoResponse;
import FarmaciaERP.Application.UseCases.BuscarOrdenTrasladoUseCase;
import FarmaciaERP.Application.UseCases.ConfirmarRecepcionUseCase;
import FarmaciaERP.Application.UseCases.GenerarOrdenTrasladoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LOG.07 - Fase 06: Gestión de stocks y distribución capilar (ME27 / MB1B).
 * Genera y despacha la Orden de Traslado (STO) desde un lote con Decisión
 * de Empleo aprobada, y registra la confirmación de recepción en el POS.
 */
@RestController
@RequestMapping("/api/logistica/ordenes-traslado")
public class OrdenTrasladoController {

    private final GenerarOrdenTrasladoUseCase generarOrdenTrasladoUseCase;
    private final ConfirmarRecepcionUseCase confirmarRecepcionUseCase;
    private final BuscarOrdenTrasladoUseCase buscarOrdenTrasladoUseCase;

    public OrdenTrasladoController(GenerarOrdenTrasladoUseCase generarOrdenTrasladoUseCase,
                                    ConfirmarRecepcionUseCase confirmarRecepcionUseCase,
                                    BuscarOrdenTrasladoUseCase buscarOrdenTrasladoUseCase) {
        this.generarOrdenTrasladoUseCase = generarOrdenTrasladoUseCase;
        this.confirmarRecepcionUseCase = confirmarRecepcionUseCase;
        this.buscarOrdenTrasladoUseCase = buscarOrdenTrasladoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> generar(@RequestBody GenerarOrdenTrasladoRequest request) {
        try {
            OrdenTrasladoResponse creada = generarOrdenTrasladoUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/confirmar-recepcion")
    public ResponseEntity<?> confirmarRecepcion(@PathVariable Long id) {
        try {
            OrdenTrasladoResponse actualizada = confirmarRecepcionUseCase.ejecutar(id);
            return ResponseEntity.ok(actualizada);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrasladoResponse> obtenerPorId(@PathVariable Long id) {
        return buscarOrdenTrasladoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrdenTrasladoResponse>> obtenerTodas(
            @RequestParam(required = false) Long inspeccionCalidadId,
            @RequestParam(required = false) Long sucursalId) {
        if (inspeccionCalidadId != null) {
            return ResponseEntity.ok(buscarOrdenTrasladoUseCase.porInspeccionCalidad(inspeccionCalidadId));
        }
        if (sucursalId != null) {
            return ResponseEntity.ok(buscarOrdenTrasladoUseCase.porSucursal(sucursalId));
        }
        return ResponseEntity.ok(buscarOrdenTrasladoUseCase.todas());
    }
}
