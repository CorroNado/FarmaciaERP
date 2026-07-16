package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CalcularDebitoARRequest;
import FarmaciaERP.Application.DTOs.Request.EvaluarJustificacionDebitoARRequest;
import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Application.UseCases.AplicarAjusteTecnicoContableUseCase;
import FarmaciaERP.Application.UseCases.BuscarDebitoARUseCase;
import FarmaciaERP.Application.UseCases.CalcularDebitoARUseCase;
import FarmaciaERP.Application.UseCases.EvaluarJustificacionDebitoARUseCase;
import FarmaciaERP.Application.UseCases.TramitarReclamoDebitoARUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FI-AR · Fase 04 — Conciliación de Débitos y Ajustes Técnicos
 * (RN-AR4-01). Los débitos generados por recetas rechazadas o
 * impugnaciones no aceptadas en la Fase 03 se evalúan: si son
 * justificados se registran contablemente; si no, se tramita el
 * reclamo ante el Área Técnica y se sienta la pérdida correspondiente,
 * cerrando el ciclo con el Ajuste Técnico Contable.
 */
@RestController
@RequestMapping("/api/fi-ar/debitos")
public class DebitoARController {

    private final CalcularDebitoARUseCase calcularDebitoARUseCase;
    private final EvaluarJustificacionDebitoARUseCase evaluarJustificacionDebitoARUseCase;
    private final TramitarReclamoDebitoARUseCase tramitarReclamoDebitoARUseCase;
    private final AplicarAjusteTecnicoContableUseCase aplicarAjusteTecnicoContableUseCase;
    private final BuscarDebitoARUseCase buscarDebitoARUseCase;

    public DebitoARController(CalcularDebitoARUseCase calcularDebitoARUseCase,
                               EvaluarJustificacionDebitoARUseCase evaluarJustificacionDebitoARUseCase,
                               TramitarReclamoDebitoARUseCase tramitarReclamoDebitoARUseCase,
                               AplicarAjusteTecnicoContableUseCase aplicarAjusteTecnicoContableUseCase,
                               BuscarDebitoARUseCase buscarDebitoARUseCase) {
        this.calcularDebitoARUseCase = calcularDebitoARUseCase;
        this.evaluarJustificacionDebitoARUseCase = evaluarJustificacionDebitoARUseCase;
        this.tramitarReclamoDebitoARUseCase = tramitarReclamoDebitoARUseCase;
        this.aplicarAjusteTecnicoContableUseCase = aplicarAjusteTecnicoContableUseCase;
        this.buscarDebitoARUseCase = buscarDebitoARUseCase;
    }

    // Cálculo del débito a partir de una receta rechazada o con débito confirmado (Fase 03)
    @PostMapping
    public ResponseEntity<?> calcular(@RequestBody CalcularDebitoARRequest request) {
        try {
            DebitoARResponse debito = calcularDebitoARUseCase.ejecutar(request);
            return new ResponseEntity<>(debito, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ¿Débito Justificado? — Sí / No
    @PostMapping("/{id}/evaluar-justificacion")
    public ResponseEntity<?> evaluarJustificacion(@PathVariable Long id,
                                                   @RequestBody EvaluarJustificacionDebitoARRequest request) {
        try {
            return ResponseEntity.ok(evaluarJustificacionDebitoARUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Tramitar Reclamo (Área Técnica) — solo débitos no justificados
    @PostMapping("/{id}/tramitar-reclamo")
    public ResponseEntity<?> tramitarReclamo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tramitarReclamoDebitoARUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Aplicar Ajustes Técnicos Contables — cierra el ciclo del débito
    @PostMapping("/{id}/aplicar-ajuste-tecnico")
    public ResponseEntity<?> aplicarAjusteTecnico(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aplicarAjusteTecnicoContableUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebitoARResponse> obtenerPorId(@PathVariable Long id) {
        return buscarDebitoARUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DebitoARResponse>> obtenerTodos(
            @RequestParam(required = false) Long contabilizacionARId) {
        if (contabilizacionARId != null) {
            return ResponseEntity.ok(buscarDebitoARUseCase.porContabilizacionAR(contabilizacionARId));
        }
        return ResponseEntity.ok(buscarDebitoARUseCase.todos());
    }

    // RN-AR4-01: indica si el lote ya puede continuar a la Fase 05 (sin débitos pendientes)
    @GetMapping("/puede-continuar-fase05")
    public ResponseEntity<Map<String, Boolean>> puedeContinuarFase05(@RequestParam Long contabilizacionARId) {
        boolean puede = buscarDebitoARUseCase.puedeContinuarFase05(contabilizacionARId);
        return ResponseEntity.ok(Map.of("puedeContinuarFase05", puede));
    }

    // RN-AR4-01: total de ajustes técnicos contables aplicados sobre el lote (usado en la Fase 06)
    @GetMapping("/ajuste-total")
    public ResponseEntity<Map<String, Double>> ajusteTotal(@RequestParam Long contabilizacionARId) {
        double total = buscarDebitoARUseCase.ajusteTotal(contabilizacionARId);
        return ResponseEntity.ok(Map.of("ajusteTotal", total));
    }
}
