package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CompararPreliquidacionRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarRecetaMedicaARRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarRespuestaAseguradoraRequest;
import FarmaciaERP.Application.DTOs.Request.ValidarTroquelesFirmasRequest;
import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Application.UseCases.BuscarRecetaMedicaARUseCase;
import FarmaciaERP.Application.UseCases.CompararPreliquidacionUseCase;
import FarmaciaERP.Application.UseCases.RegistrarRecetaMedicaARUseCase;
import FarmaciaERP.Application.UseCases.RegistrarRespuestaAseguradoraUseCase;
import FarmaciaERP.Application.UseCases.ValidarTroquelesFirmasUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FI-AR · Fase 03 — Auditoría Médica e Impugnación de Recetas
 * (RN-AR3-01 · ZFMR_RECHAZO / ZFMR_IMPUGNACION). Cada receta física del
 * lote consolidado en la Fase 02 se valida y coteja contra la
 * pre-liquidación de la aseguradora antes de habilitar la Fase 04.
 */
@RestController
@RequestMapping("/api/fi-ar/recetas")
public class RecetaMedicaARController {

    private final RegistrarRecetaMedicaARUseCase registrarRecetaMedicaARUseCase;
    private final ValidarTroquelesFirmasUseCase validarTroquelesFirmasUseCase;
    private final CompararPreliquidacionUseCase compararPreliquidacionUseCase;
    private final RegistrarRespuestaAseguradoraUseCase registrarRespuestaAseguradoraUseCase;
    private final BuscarRecetaMedicaARUseCase buscarRecetaMedicaARUseCase;

    public RecetaMedicaARController(RegistrarRecetaMedicaARUseCase registrarRecetaMedicaARUseCase,
                                     ValidarTroquelesFirmasUseCase validarTroquelesFirmasUseCase,
                                     CompararPreliquidacionUseCase compararPreliquidacionUseCase,
                                     RegistrarRespuestaAseguradoraUseCase registrarRespuestaAseguradoraUseCase,
                                     BuscarRecetaMedicaARUseCase buscarRecetaMedicaARUseCase) {
        this.registrarRecetaMedicaARUseCase = registrarRecetaMedicaARUseCase;
        this.validarTroquelesFirmasUseCase = validarTroquelesFirmasUseCase;
        this.compararPreliquidacionUseCase = compararPreliquidacionUseCase;
        this.registrarRespuestaAseguradoraUseCase = registrarRespuestaAseguradoraUseCase;
        this.buscarRecetaMedicaARUseCase = buscarRecetaMedicaARUseCase;
    }

    // Recepción física de la receta médica dentro del lote consolidado de la Fase 02
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistrarRecetaMedicaARRequest request) {
        try {
            RecetaMedicaARResponse receta = registrarRecetaMedicaARUseCase.ejecutar(request);
            return new ResponseEntity<>(receta, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Validación de Troqueles, Firmas y Vigencia
    @PostMapping("/{id}/validar-troqueles-firmas")
    public ResponseEntity<?> validarTroquelesFirmas(@PathVariable Long id,
                                                     @RequestBody ValidarTroquelesFirmasRequest request) {
        try {
            return ResponseEntity.ok(validarTroquelesFirmasUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Comparación contra la Pre-liquidación de la Aseguradora
    @PostMapping("/{id}/comparar-preliquidacion")
    public ResponseEntity<?> compararPreliquidacion(@PathVariable Long id,
                                                     @RequestBody CompararPreliquidacionRequest request) {
        try {
            return ResponseEntity.ok(compararPreliquidacionUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Registro de la Respuesta de la Aseguradora a la impugnación
    @PostMapping("/{id}/respuesta-aseguradora")
    public ResponseEntity<?> registrarRespuestaAseguradora(@PathVariable Long id,
                                                            @RequestBody RegistrarRespuestaAseguradoraRequest request) {
        try {
            return ResponseEntity.ok(registrarRespuestaAseguradoraUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaMedicaARResponse> obtenerPorId(@PathVariable Long id) {
        return buscarRecetaMedicaARUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RecetaMedicaARResponse>> obtenerTodas(
            @RequestParam(required = false) Long contabilizacionARId) {
        if (contabilizacionARId != null) {
            return ResponseEntity.ok(buscarRecetaMedicaARUseCase.porContabilizacionAR(contabilizacionARId));
        }
        return ResponseEntity.ok(buscarRecetaMedicaARUseCase.todas());
    }

    // RN-AR3-01: indica si todas las recetas del lote ya están en estado terminal (habilita Fase 04)
    @GetMapping("/puede-continuar-fase04")
    public ResponseEntity<Map<String, Boolean>> puedeContinuarFase04(@RequestParam Long contabilizacionARId) {
        boolean puede = buscarRecetaMedicaARUseCase.puedeContinuarFase04(contabilizacionARId);
        return ResponseEntity.ok(Map.of("puedeContinuarFase04", puede));
    }
}
