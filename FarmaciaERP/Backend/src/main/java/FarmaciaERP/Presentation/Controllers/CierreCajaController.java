package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.AbrirCierreCajaRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarArqueoRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarJustificacionRequest;
import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Application.UseCases.AbrirCierreCajaUseCase;
import FarmaciaERP.Application.UseCases.BuscarCierreCajaUseCase;
import FarmaciaERP.Application.UseCases.ClasificarCopagoCoberturaUseCase;
import FarmaciaERP.Application.UseCases.EnviarFisicosRecetasUseCase;
import FarmaciaERP.Application.UseCases.RegistrarArqueoUseCase;
import FarmaciaERP.Application.UseCases.RegistrarJustificacionUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD).
 * RN-AR1-01: expone el ciclo completo de la Fase 01, desde la emisión del
 * reporte consolidado de ventas hasta la clasificación automática de
 * copagos y coberturas que habilita la Fase 02.
 */
@RestController
@RequestMapping("/api/fi-ar/cierre-caja")
public class CierreCajaController {

    private final AbrirCierreCajaUseCase abrirCierreCajaUseCase;
    private final RegistrarArqueoUseCase registrarArqueoUseCase;
    private final RegistrarJustificacionUseCase registrarJustificacionUseCase;
    private final EnviarFisicosRecetasUseCase enviarFisicosRecetasUseCase;
    private final ClasificarCopagoCoberturaUseCase clasificarCopagoCoberturaUseCase;
    private final BuscarCierreCajaUseCase buscarCierreCajaUseCase;

    public CierreCajaController(AbrirCierreCajaUseCase abrirCierreCajaUseCase,
                                 RegistrarArqueoUseCase registrarArqueoUseCase,
                                 RegistrarJustificacionUseCase registrarJustificacionUseCase,
                                 EnviarFisicosRecetasUseCase enviarFisicosRecetasUseCase,
                                 ClasificarCopagoCoberturaUseCase clasificarCopagoCoberturaUseCase,
                                 BuscarCierreCajaUseCase buscarCierreCajaUseCase) {
        this.abrirCierreCajaUseCase = abrirCierreCajaUseCase;
        this.registrarArqueoUseCase = registrarArqueoUseCase;
        this.registrarJustificacionUseCase = registrarJustificacionUseCase;
        this.enviarFisicosRecetasUseCase = enviarFisicosRecetasUseCase;
        this.clasificarCopagoCoberturaUseCase = clasificarCopagoCoberturaUseCase;
        this.buscarCierreCajaUseCase = buscarCierreCajaUseCase;
    }

    // 1.1 - Emitir Reporte Consolidado de Ventas del Mostrador
    @PostMapping
    public ResponseEntity<?> abrir(@RequestBody AbrirCierreCajaRequest request) {
        try {
            CierreCajaResponse cierre = abrirCierreCajaUseCase.ejecutar(request);
            return new ResponseEntity<>(cierre, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 1.2 - Realizar Arqueo Físico vs. Valores Registrados en Pantalla
    @PostMapping("/{id}/arqueo")
    public ResponseEntity<?> registrarArqueo(@PathVariable Long id, @RequestBody RegistrarArqueoRequest request) {
        try {
            CierreCajaResponse cierre = registrarArqueoUseCase.ejecutar(id, request);
            return ResponseEntity.ok(cierre);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.1 - Registrar Justificación de Faltante o Sobrante en Sistema
    @PostMapping("/{id}/justificacion")
    public ResponseEntity<?> registrarJustificacion(@PathVariable Long id,
                                                     @RequestBody RegistrarJustificacionRequest request) {
        try {
            CierreCajaResponse cierre = registrarJustificacionUseCase.ejecutar(id, request);
            return ResponseEntity.ok(cierre);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Fase 1 (rama de variación) - Enviar Físicos de Recetas a Oficina Central
    @PostMapping("/{id}/enviar-fisicos-recetas")
    public ResponseEntity<?> enviarFisicosRecetas(@PathVariable Long id) {
        try {
            CierreCajaResponse cierre = enviarFisicosRecetasUseCase.ejecutar(id);
            return ResponseEntity.ok(cierre);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 1.3 - Clasificar de forma Automática Copagos y Coberturas de Aseguradoras
    @PostMapping("/{id}/clasificar-copago-cobertura")
    public ResponseEntity<?> clasificarCopagoCobertura(@PathVariable Long id) {
        try {
            CierreCajaResponse cierre = clasificarCopagoCoberturaUseCase.ejecutar(id);
            return ResponseEntity.ok(cierre);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CierreCajaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCierreCajaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CierreCajaResponse>> obtenerTodos(
            @RequestParam(required = false) Long sucursalId) {
        if (sucursalId != null) {
            return ResponseEntity.ok(buscarCierreCajaUseCase.porSucursal(sucursalId));
        }
        return ResponseEntity.ok(buscarCierreCajaUseCase.todas());
    }
}
