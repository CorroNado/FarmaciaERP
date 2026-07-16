package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.IniciarAjusteContableRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarRecepcionNotaCreditoRequest;
import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Application.UseCases.BuscarAjusteContableUseCase;
import FarmaciaERP.Application.UseCases.DesbloquearPartidaUseCase;
import FarmaciaERP.Application.UseCases.EjecutarAsientoRegularizacionUseCase;
import FarmaciaERP.Application.UseCases.EvaluarYEnviarNotaCreditoUseCase;
import FarmaciaERP.Application.UseCases.GestionarReclamoUseCase;
import FarmaciaERP.Application.UseCases.IniciarAjusteContableUseCase;
import FarmaciaERP.Application.UseCases.RegistrarNotaCreditoUseCase;
import FarmaciaERP.Application.UseCases.RegistrarRecepcionNotaCreditoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.03 - Fase 03: Ajustes Contables y Regularización (Cierre de
 * Transacción). El Área Financiera confirma la recepción de la Nota de
 * Crédito del laboratorio/droguería; si no llega, se gestiona el reclamo
 * directamente. En ambos casos se ejecuta el asiento contable y se
 * desbloquea la factura para pago, habilitando la Fase 04.
 */
@RestController
@RequestMapping("/api/fi-ap/ajustes-contables")
public class AjusteContableRegularizacionController {

    private final IniciarAjusteContableUseCase iniciarAjusteContableUseCase;
    private final RegistrarRecepcionNotaCreditoUseCase registrarRecepcionNotaCreditoUseCase;
    private final GestionarReclamoUseCase gestionarReclamoUseCase;
    private final EvaluarYEnviarNotaCreditoUseCase evaluarYEnviarNotaCreditoUseCase;
    private final RegistrarNotaCreditoUseCase registrarNotaCreditoUseCase;
    private final EjecutarAsientoRegularizacionUseCase ejecutarAsientoRegularizacionUseCase;
    private final DesbloquearPartidaUseCase desbloquearPartidaUseCase;
    private final BuscarAjusteContableUseCase buscarAjusteContableUseCase;

    public AjusteContableRegularizacionController(IniciarAjusteContableUseCase iniciarAjusteContableUseCase,
                                                    RegistrarRecepcionNotaCreditoUseCase registrarRecepcionNotaCreditoUseCase,
                                                    GestionarReclamoUseCase gestionarReclamoUseCase,
                                                    EvaluarYEnviarNotaCreditoUseCase evaluarYEnviarNotaCreditoUseCase,
                                                    RegistrarNotaCreditoUseCase registrarNotaCreditoUseCase,
                                                    EjecutarAsientoRegularizacionUseCase ejecutarAsientoRegularizacionUseCase,
                                                    DesbloquearPartidaUseCase desbloquearPartidaUseCase,
                                                    BuscarAjusteContableUseCase buscarAjusteContableUseCase) {
        this.iniciarAjusteContableUseCase = iniciarAjusteContableUseCase;
        this.registrarRecepcionNotaCreditoUseCase = registrarRecepcionNotaCreditoUseCase;
        this.gestionarReclamoUseCase = gestionarReclamoUseCase;
        this.evaluarYEnviarNotaCreditoUseCase = evaluarYEnviarNotaCreditoUseCase;
        this.registrarNotaCreditoUseCase = registrarNotaCreditoUseCase;
        this.ejecutarAsientoRegularizacionUseCase = ejecutarAsientoRegularizacionUseCase;
        this.desbloquearPartidaUseCase = desbloquearPartidaUseCase;
        this.buscarAjusteContableUseCase = buscarAjusteContableUseCase;
    }

    // Inicio del cierre de transacción - a partir de la disputa resuelta en Fase 02
    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarAjusteContableRequest request) {
        try {
            AjusteContableRegularizacionResponse ajuste = iniciarAjusteContableUseCase.ejecutar(request);
            return new ResponseEntity<>(ajuste, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.1 - ¿Se Recibe Nota de Crédito? (Sí / No)
    @PostMapping("/{id}/recepcion-nota-credito")
    public ResponseEntity<?> registrarRecepcionNotaCredito(@PathVariable Long id,
                                                             @RequestBody RegistrarRecepcionNotaCreditoRequest request) {
        try {
            return ResponseEntity.ok(registrarRecepcionNotaCreditoUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.1.a - Gestionar Reclamo con el Laboratorio/Droguería
    @PostMapping("/{id}/gestionar-reclamo")
    public ResponseEntity<?> gestionarReclamo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gestionarReclamoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.1.a (cont.) - Laboratorio/Droguería evalúa y envía la Nota de Crédito
    @PostMapping("/{id}/evaluar-envio-nota-credito")
    public ResponseEntity<?> evaluarYEnviarNotaCredito(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(evaluarYEnviarNotaCreditoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.1.b - Registrar Nota de Crédito en SAP (recibida directamente)
    @PostMapping("/{id}/registrar-nota-credito")
    public ResponseEntity<?> registrarNotaCredito(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(registrarNotaCreditoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.2 - Ejecutar Asiento de Regularización por diferencias permitidas
    @PostMapping("/{id}/asiento-regularizacion")
    public ResponseEntity<?> ejecutarAsientoRegularizacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ejecutarAsientoRegularizacionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.3 - Desbloquear Partida Presupuestaria y Actualizar Estado de Pago
    @PostMapping("/{id}/desbloquear-partida")
    public ResponseEntity<?> desbloquearPartida(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(desbloquearPartidaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteContableRegularizacionResponse> obtenerPorId(@PathVariable Long id) {
        return buscarAjusteContableUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AjusteContableRegularizacionResponse>> obtenerTodos(
            @RequestParam(required = false) Long disputaComercialId) {
        if (disputaComercialId != null) {
            return ResponseEntity.ok(buscarAjusteContableUseCase.porDisputaComercial(disputaComercialId));
        }
        return ResponseEntity.ok(buscarAjusteContableUseCase.todos());
    }
}
