package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.AplicarCompensacionAutomaticaRequest;
import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Application.UseCases.AplicarCompensacionAutomaticaUseCase;
import FarmaciaERP.Application.UseCases.BuscarCompensacionARUseCase;
import FarmaciaERP.Application.UseCases.CerrarIngresosConvenioUseCase;
import FarmaciaERP.Application.UseCases.ConfirmarSaldoLimpioUseCase;
import FarmaciaERP.Application.UseCases.GenerarReporteRendimientoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FI-AR · Fase 06 — Compensación Final y Análisis de Margen Neto
 * (RN-AR6-01). Cierre del ciclo FI-AR del período: Tesorería aplica
 * la compensación automática sobre la cuenta del cliente, se genera
 * el Reporte de Rendimiento Comercial y Margen de la Cadena, se
 * confirma el saldo limpio en cuentas corrientes y se cierran los
 * ingresos por convenio.
 */
@RestController
@RequestMapping("/api/fi-ar/compensacion-final")
public class CompensacionARController {

    private final AplicarCompensacionAutomaticaUseCase aplicarCompensacionAutomaticaUseCase;
    private final GenerarReporteRendimientoUseCase generarReporteRendimientoUseCase;
    private final ConfirmarSaldoLimpioUseCase confirmarSaldoLimpioUseCase;
    private final CerrarIngresosConvenioUseCase cerrarIngresosConvenioUseCase;
    private final BuscarCompensacionARUseCase buscarCompensacionARUseCase;

    public CompensacionARController(AplicarCompensacionAutomaticaUseCase aplicarCompensacionAutomaticaUseCase,
                                     GenerarReporteRendimientoUseCase generarReporteRendimientoUseCase,
                                     ConfirmarSaldoLimpioUseCase confirmarSaldoLimpioUseCase,
                                     CerrarIngresosConvenioUseCase cerrarIngresosConvenioUseCase,
                                     BuscarCompensacionARUseCase buscarCompensacionARUseCase) {
        this.aplicarCompensacionAutomaticaUseCase = aplicarCompensacionAutomaticaUseCase;
        this.generarReporteRendimientoUseCase = generarReporteRendimientoUseCase;
        this.confirmarSaldoLimpioUseCase = confirmarSaldoLimpioUseCase;
        this.cerrarIngresosConvenioUseCase = cerrarIngresosConvenioUseCase;
        this.buscarCompensacionARUseCase = buscarCompensacionARUseCase;
    }

    // 6.1 - Aplicar Compensación Automática sobre la Cuenta del Cliente (Tesorería)
    @PostMapping
    public ResponseEntity<?> aplicarCompensacion(@RequestBody AplicarCompensacionAutomaticaRequest request) {
        try {
            CompensacionARResponse compensacion = aplicarCompensacionAutomaticaUseCase.ejecutar(request);
            return new ResponseEntity<>(compensacion, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Generar Reporte de Rendimiento Comercial y Margen de la Cadena
    @PostMapping("/{id}/generar-reporte")
    public ResponseEntity<?> generarReporte(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(generarReporteRendimientoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Confirmar Saldo Limpio en Cuentas Corrientes
    @PostMapping("/{id}/confirmar-saldo")
    public ResponseEntity<?> confirmarSaldo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(confirmarSaldoLimpioUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cerrar Ingresos por Convenio — finaliza el ciclo FI-AR del período
    @PostMapping("/{id}/cerrar-ingresos")
    public ResponseEntity<?> cerrarIngresos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cerrarIngresosConvenioUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompensacionARResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCompensacionARUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosOPorLote(@RequestParam(required = false) Long contabilizacionARId) {
        if (contabilizacionARId != null) {
            return buscarCompensacionARUseCase.porContabilizacionAR(contabilizacionARId)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(buscarCompensacionARUseCase.todos());
    }

    // RN-AR6-01: indica si el ciclo FI-AR del lote quedó finalizado (cierre de ingresos completado)
    @GetMapping("/ciclo-finalizado")
    public ResponseEntity<Map<String, Boolean>> cicloFinalizado(@RequestParam Long contabilizacionARId) {
        boolean finalizado = buscarCompensacionARUseCase.cicloFinalizado(contabilizacionARId);
        return ResponseEntity.ok(Map.of("cicloFinalizado", finalizado));
    }
}
