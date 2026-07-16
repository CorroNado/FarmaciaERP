package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.AuditarRecetasRequest;
import FarmaciaERP.Application.DTOs.Request.IniciarContabilizacionARRequest;
import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Application.UseCases.AuditarRecetasUseCase;
import FarmaciaERP.Application.UseCases.BuscarContabilizacionARUseCase;
import FarmaciaERP.Application.UseCases.ConciliarLotesPOSUseCase;
import FarmaciaERP.Application.UseCases.ConsolidarLoteDespacharValijaUseCase;
import FarmaciaERP.Application.UseCases.IniciarContabilizacionARUseCase;
import FarmaciaERP.Application.UseCases.ProcesarAsientoContableUseCase;
import FarmaciaERP.Application.UseCases.ReintentarAuditoriaUseCase;
import FarmaciaERP.Application.UseCases.RevisarAjusteAsientosUseCase;
import FarmaciaERP.Application.UseCases.SolicitarDuplicadoRecetaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AR · Fase 02 — Contabilización y Declaración de Valores (SAP FI-AR).
 * RN-AR2-01: expone el ciclo completo de la Fase 02, desde la conciliación
 * de lotes POS hasta la consolidación del lote y despacho de la valija
 * física que habilita la Fase 03.
 */
@RestController
@RequestMapping("/api/fi-ar/contabilizacion")
public class ContabilizacionARController {

    private final IniciarContabilizacionARUseCase iniciarContabilizacionARUseCase;
    private final ConciliarLotesPOSUseCase conciliarLotesPOSUseCase;
    private final ProcesarAsientoContableUseCase procesarAsientoContableUseCase;
    private final RevisarAjusteAsientosUseCase revisarAjusteAsientosUseCase;
    private final AuditarRecetasUseCase auditarRecetasUseCase;
    private final SolicitarDuplicadoRecetaUseCase solicitarDuplicadoRecetaUseCase;
    private final ReintentarAuditoriaUseCase reintentarAuditoriaUseCase;
    private final ConsolidarLoteDespacharValijaUseCase consolidarLoteDespacharValijaUseCase;
    private final BuscarContabilizacionARUseCase buscarContabilizacionARUseCase;

    public ContabilizacionARController(IniciarContabilizacionARUseCase iniciarContabilizacionARUseCase,
                                        ConciliarLotesPOSUseCase conciliarLotesPOSUseCase,
                                        ProcesarAsientoContableUseCase procesarAsientoContableUseCase,
                                        RevisarAjusteAsientosUseCase revisarAjusteAsientosUseCase,
                                        AuditarRecetasUseCase auditarRecetasUseCase,
                                        SolicitarDuplicadoRecetaUseCase solicitarDuplicadoRecetaUseCase,
                                        ReintentarAuditoriaUseCase reintentarAuditoriaUseCase,
                                        ConsolidarLoteDespacharValijaUseCase consolidarLoteDespacharValijaUseCase,
                                        BuscarContabilizacionARUseCase buscarContabilizacionARUseCase) {
        this.iniciarContabilizacionARUseCase = iniciarContabilizacionARUseCase;
        this.conciliarLotesPOSUseCase = conciliarLotesPOSUseCase;
        this.procesarAsientoContableUseCase = procesarAsientoContableUseCase;
        this.revisarAjusteAsientosUseCase = revisarAjusteAsientosUseCase;
        this.auditarRecetasUseCase = auditarRecetasUseCase;
        this.solicitarDuplicadoRecetaUseCase = solicitarDuplicadoRecetaUseCase;
        this.reintentarAuditoriaUseCase = reintentarAuditoriaUseCase;
        this.consolidarLoteDespacharValijaUseCase = consolidarLoteDespacharValijaUseCase;
        this.buscarContabilizacionARUseCase = buscarContabilizacionARUseCase;
    }

    // Inicia la Fase 02 a partir de un cierre de caja de la Fase 01
    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarContabilizacionARRequest request) {
        try {
            ContabilizacionARResponse contabilizacion = iniciarContabilizacionARUseCase.ejecutar(request);
            return new ResponseEntity<>(contabilizacion, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.1.1 - Conciliación Primaria de Lotes de Tarjetas Físicas (POS)
    @PostMapping("/{id}/conciliar-lotes-pos")
    public ResponseEntity<?> conciliarLotesPOS(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(conciliarLotesPOSUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.2 - Procesar Asiento Automatizado de Ventas y Cuadraturas
    @PostMapping("/{id}/procesar-asiento")
    public ResponseEntity<?> procesarAsientoContable(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(procesarAsientoContableUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.3 - Revisión y Ajuste de Asientos Descuadrados
    @PostMapping("/{id}/revisar-ajuste-asientos")
    public ResponseEntity<?> revisarAjusteAsientos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(revisarAjusteAsientosUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.3.1 - Auditoría de Integridad y Firmas de Recetas Médicas
    @PostMapping("/{id}/auditar-recetas")
    public ResponseEntity<?> auditarRecetas(@PathVariable Long id, @RequestBody AuditarRecetasRequest request) {
        try {
            return ResponseEntity.ok(auditarRecetasUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.3.3 - Subsanación de Recetas y Solicitud de Duplicados a Sucursal / Médico
    @PostMapping("/{id}/solicitar-duplicado-receta")
    public ResponseEntity<?> solicitarDuplicadoReceta(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(solicitarDuplicadoRecetaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reintento de la auditoría de integridad tras recibir el duplicado
    @PostMapping("/{id}/reintentar-auditoria")
    public ResponseEntity<?> reintentarAuditoria(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reintentarAuditoriaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.3.2 - Consolidación del Lote y Despacho de Valija Física
    @PostMapping("/{id}/consolidar-lote")
    public ResponseEntity<?> consolidarLoteDespacharValija(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(consolidarLoteDespacharValijaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContabilizacionARResponse> obtenerPorId(@PathVariable Long id) {
        return buscarContabilizacionARUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) Long cierreCajaId) {
        if (cierreCajaId != null) {
            return buscarContabilizacionARUseCase.porCierreCaja(cierreCajaId)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        List<ContabilizacionARResponse> todas = buscarContabilizacionARUseCase.todas();
        return ResponseEntity.ok(todas);
    }
}
