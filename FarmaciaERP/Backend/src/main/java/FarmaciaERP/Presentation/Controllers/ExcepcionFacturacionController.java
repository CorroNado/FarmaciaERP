package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CapturarExcepcionFacturacionRequest;
import FarmaciaERP.Application.DTOs.Request.ClasificarExcepcionFacturacionRequest;
import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Application.UseCases.BuscarExcepcionFacturacionUseCase;
import FarmaciaERP.Application.UseCases.CapturarExcepcionFacturacionUseCase;
import FarmaciaERP.Application.UseCases.ClasificarExcepcionFacturacionUseCase;
import FarmaciaERP.Application.UseCases.RevisarExcepcionFacturacionUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.01 - Fase 01: Captura de Excepciones de Facturación (Frontera
 * Logística). Expone el panel de facturas bloqueadas por desviación de
 * precio/cantidad y el flujo de revisión + clasificación del Analista de
 * Cuentas por Pagar.
 */
@RestController
@RequestMapping("/api/fi-ap/excepciones-facturacion")
public class ExcepcionFacturacionController {

    private final CapturarExcepcionFacturacionUseCase capturarExcepcionFacturacionUseCase;
    private final RevisarExcepcionFacturacionUseCase revisarExcepcionFacturacionUseCase;
    private final ClasificarExcepcionFacturacionUseCase clasificarExcepcionFacturacionUseCase;
    private final BuscarExcepcionFacturacionUseCase buscarExcepcionFacturacionUseCase;

    public ExcepcionFacturacionController(CapturarExcepcionFacturacionUseCase capturarExcepcionFacturacionUseCase,
                                           RevisarExcepcionFacturacionUseCase revisarExcepcionFacturacionUseCase,
                                           ClasificarExcepcionFacturacionUseCase clasificarExcepcionFacturacionUseCase,
                                           BuscarExcepcionFacturacionUseCase buscarExcepcionFacturacionUseCase) {
        this.capturarExcepcionFacturacionUseCase = capturarExcepcionFacturacionUseCase;
        this.revisarExcepcionFacturacionUseCase = revisarExcepcionFacturacionUseCase;
        this.clasificarExcepcionFacturacionUseCase = clasificarExcepcionFacturacionUseCase;
        this.buscarExcepcionFacturacionUseCase = buscarExcepcionFacturacionUseCase;
    }

    /** Sistema ERP: captura automáticamente la excepción desde una conciliación bloqueada en MRBR. */
    @PostMapping
    public ResponseEntity<?> capturar(@RequestBody CapturarExcepcionFacturacionRequest request) {
        try {
            ExcepcionFacturacionResponse resultado = capturarExcepcionFacturacionUseCase.capturar(request);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Analista AP · paso 1.1: Revisar Panel de Facturas Bloqueadas. */
    @PostMapping("/{id}/revisar")
    public ResponseEntity<?> revisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(revisarExcepcionFacturacionUseCase.revisar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Analista AP · paso 1.2: Analizar y Clasificar Discrepancia (dispara notificación 1.3). */
    @PostMapping("/{id}/clasificar")
    public ResponseEntity<?> clasificar(@PathVariable Long id,
                                         @RequestBody ClasificarExcepcionFacturacionRequest request) {
        try {
            return ResponseEntity.ok(clasificarExcepcionFacturacionUseCase.clasificar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcepcionFacturacionResponse> obtenerPorId(@PathVariable Long id) {
        return buscarExcepcionFacturacionUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ExcepcionFacturacionResponse>> obtenerTodas(
            @RequestParam(required = false) Long conciliacionTresViasId) {
        if (conciliacionTresViasId != null) {
            return ResponseEntity.ok(buscarExcepcionFacturacionUseCase.porConciliacionTresVias(conciliacionTresViasId));
        }
        return ResponseEntity.ok(buscarExcepcionFacturacionUseCase.todas());
    }
}
