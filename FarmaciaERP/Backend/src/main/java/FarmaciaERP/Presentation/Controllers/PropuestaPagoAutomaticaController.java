package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.IniciarPropuestaPagoRequest;
import FarmaciaERP.Application.DTOs.Request.IntroducirParametrosPagoRequest;
import FarmaciaERP.Application.DTOs.Response.PropuestaPagoAutomaticaResponse;
import FarmaciaERP.Application.UseCases.AjustarParametrosYReejecutarUseCase;
import FarmaciaERP.Application.UseCases.AprobarPropuestaFinalUseCase;
import FarmaciaERP.Application.UseCases.BuscarPropuestaPagoUseCase;
import FarmaciaERP.Application.UseCases.EjecutarPagoPropuestaUseCase;
import FarmaciaERP.Application.UseCases.EjecutarPropuestaAutomaticaUseCase;
import FarmaciaERP.Application.UseCases.GenerarArchivosBancariosUseCase;
import FarmaciaERP.Application.UseCases.IniciarPropuestaPagoUseCase;
import FarmaciaERP.Application.UseCases.IntroducirParametrosPagoUseCase;
import FarmaciaERP.Application.UseCases.RevisarReporteExcepcionesUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.05 - Fase 05: Procesamiento Automático y Propuesta de Pago
 * (RN-AP5-01 · F110). A partir del lote de pagos conciliado a nivel de
 * gestión en la Fase 04, el Analista de Cuentas por Pagar introduce los
 * parámetros de pago y el sistema ERP ejecuta la propuesta de pago
 * automática; el analista revisa el reporte de excepciones y bloqueos
 * antes de aprobar la propuesta final, ejecutar el pago y generar los
 * archivos bancarios que habilitan la Fase 06.
 */
@RestController
@RequestMapping("/api/fi-ap/propuestas-pago")
public class PropuestaPagoAutomaticaController {

    private final IniciarPropuestaPagoUseCase iniciarPropuestaPagoUseCase;
    private final IntroducirParametrosPagoUseCase introducirParametrosPagoUseCase;
    private final EjecutarPropuestaAutomaticaUseCase ejecutarPropuestaAutomaticaUseCase;
    private final RevisarReporteExcepcionesUseCase revisarReporteExcepcionesUseCase;
    private final AjustarParametrosYReejecutarUseCase ajustarParametrosYReejecutarUseCase;
    private final AprobarPropuestaFinalUseCase aprobarPropuestaFinalUseCase;
    private final EjecutarPagoPropuestaUseCase ejecutarPagoPropuestaUseCase;
    private final GenerarArchivosBancariosUseCase generarArchivosBancariosUseCase;
    private final BuscarPropuestaPagoUseCase buscarPropuestaPagoUseCase;

    public PropuestaPagoAutomaticaController(IniciarPropuestaPagoUseCase iniciarPropuestaPagoUseCase,
                                              IntroducirParametrosPagoUseCase introducirParametrosPagoUseCase,
                                              EjecutarPropuestaAutomaticaUseCase ejecutarPropuestaAutomaticaUseCase,
                                              RevisarReporteExcepcionesUseCase revisarReporteExcepcionesUseCase,
                                              AjustarParametrosYReejecutarUseCase ajustarParametrosYReejecutarUseCase,
                                              AprobarPropuestaFinalUseCase aprobarPropuestaFinalUseCase,
                                              EjecutarPagoPropuestaUseCase ejecutarPagoPropuestaUseCase,
                                              GenerarArchivosBancariosUseCase generarArchivosBancariosUseCase,
                                              BuscarPropuestaPagoUseCase buscarPropuestaPagoUseCase) {
        this.iniciarPropuestaPagoUseCase = iniciarPropuestaPagoUseCase;
        this.introducirParametrosPagoUseCase = introducirParametrosPagoUseCase;
        this.ejecutarPropuestaAutomaticaUseCase = ejecutarPropuestaAutomaticaUseCase;
        this.revisarReporteExcepcionesUseCase = revisarReporteExcepcionesUseCase;
        this.ajustarParametrosYReejecutarUseCase = ajustarParametrosYReejecutarUseCase;
        this.aprobarPropuestaFinalUseCase = aprobarPropuestaFinalUseCase;
        this.ejecutarPagoPropuestaUseCase = ejecutarPagoPropuestaUseCase;
        this.generarArchivosBancariosUseCase = generarArchivosBancariosUseCase;
        this.buscarPropuestaPagoUseCase = buscarPropuestaPagoUseCase;
    }

    // Inicio de la propuesta - a partir del lote de pagos conciliado en Fase 04
    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarPropuestaPagoRequest request) {
        try {
            PropuestaPagoAutomaticaResponse propuesta = iniciarPropuestaPagoUseCase.ejecutar(request);
            return new ResponseEntity<>(propuesta, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.1 - Introducir Parámetros de Pago en F110
    @PostMapping("/{id}/parametros")
    public ResponseEntity<?> introducirParametrosPago(@PathVariable Long id,
                                                        @RequestBody IntroducirParametrosPagoRequest request) {
        try {
            return ResponseEntity.ok(introducirParametrosPagoUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.1 (cont.) - Ejecutar Propuesta de Pago Automática
    @PostMapping("/{id}/ejecutar-propuesta")
    public ResponseEntity<?> ejecutarPropuestaAutomatica(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ejecutarPropuestaAutomaticaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.2 - Revisar Reporte de Excepciones y Bloqueos
    @PostMapping("/{id}/revisar-excepciones")
    public ResponseEntity<?> revisarReporteExcepciones(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(revisarReporteExcepcionesUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.2 (cont.) - Ajustar Parámetros y Reejecutar Propuesta
    @PostMapping("/{id}/ajustar-reejecutar")
    public ResponseEntity<?> ajustarParametrosYReejecutar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ajustarParametrosYReejecutarUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.2 (cont.) - Aprobar Propuesta de Pago Final
    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarPropuestaFinal(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aprobarPropuestaFinalUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.3 - Ejecutar Ejecución de Pago
    @PostMapping("/{id}/ejecutar-pago")
    public ResponseEntity<?> ejecutarPago(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ejecutarPagoPropuestaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.3 (cont.) - Generar Archivos Bancarios Planos (IDoc / N43)
    @PostMapping("/{id}/generar-archivos")
    public ResponseEntity<?> generarArchivosBancarios(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(generarArchivosBancariosUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropuestaPagoAutomaticaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarPropuestaPagoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PropuestaPagoAutomaticaResponse>> obtenerTodos() {
        return ResponseEntity.ok(buscarPropuestaPagoUseCase.todos());
    }
}
