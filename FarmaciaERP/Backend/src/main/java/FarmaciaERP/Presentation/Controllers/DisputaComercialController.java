package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.AbrirDisputaComercialRequest;
import FarmaciaERP.Application.DTOs.Request.RegistrarContraofertaRequest;
import FarmaciaERP.Application.DTOs.Response.DisputaComercialResponse;
import FarmaciaERP.Application.UseCases.AbrirDisputaComercialUseCase;
import FarmaciaERP.Application.UseCases.AbrirNegociacionUseCase;
import FarmaciaERP.Application.UseCases.AceptarAbsorcionUseCase;
import FarmaciaERP.Application.UseCases.BuscarDisputaComercialUseCase;
import FarmaciaERP.Application.UseCases.CotejarFacturaContratoUseCase;
import FarmaciaERP.Application.UseCases.CuantificarImpactoFinancieroUseCase;
import FarmaciaERP.Application.UseCases.ReabrirNegociacionUseCase;
import FarmaciaERP.Application.UseCases.RegistrarContraofertaUseCase;
import FarmaciaERP.Application.UseCases.ResolverWorkflowDisputaUseCase;
import FarmaciaERP.Application.UseCases.ValidarDesviacionUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.02 - Fase 02: Gestión Humana de Disputas Comerciales (Workflow
 * ERP). El Comprador / Category Manager coteja, cuantifica y negocia con
 * el proveedor hasta acordar la absorción de la diferencia comercial.
 */
@RestController
@RequestMapping("/api/fi-ap/disputas-comerciales")
public class DisputaComercialController {

    private final AbrirDisputaComercialUseCase abrirDisputaComercialUseCase;
    private final CotejarFacturaContratoUseCase cotejarFacturaContratoUseCase;
    private final CuantificarImpactoFinancieroUseCase cuantificarImpactoFinancieroUseCase;
    private final ValidarDesviacionUseCase validarDesviacionUseCase;
    private final AbrirNegociacionUseCase abrirNegociacionUseCase;
    private final RegistrarContraofertaUseCase registrarContraofertaUseCase;
    private final AceptarAbsorcionUseCase aceptarAbsorcionUseCase;
    private final ReabrirNegociacionUseCase reabrirNegociacionUseCase;
    private final ResolverWorkflowDisputaUseCase resolverWorkflowDisputaUseCase;
    private final BuscarDisputaComercialUseCase buscarDisputaComercialUseCase;

    public DisputaComercialController(AbrirDisputaComercialUseCase abrirDisputaComercialUseCase,
                                       CotejarFacturaContratoUseCase cotejarFacturaContratoUseCase,
                                       CuantificarImpactoFinancieroUseCase cuantificarImpactoFinancieroUseCase,
                                       ValidarDesviacionUseCase validarDesviacionUseCase,
                                       AbrirNegociacionUseCase abrirNegociacionUseCase,
                                       RegistrarContraofertaUseCase registrarContraofertaUseCase,
                                       AceptarAbsorcionUseCase aceptarAbsorcionUseCase,
                                       ReabrirNegociacionUseCase reabrirNegociacionUseCase,
                                       ResolverWorkflowDisputaUseCase resolverWorkflowDisputaUseCase,
                                       BuscarDisputaComercialUseCase buscarDisputaComercialUseCase) {
        this.abrirDisputaComercialUseCase = abrirDisputaComercialUseCase;
        this.cotejarFacturaContratoUseCase = cotejarFacturaContratoUseCase;
        this.cuantificarImpactoFinancieroUseCase = cuantificarImpactoFinancieroUseCase;
        this.validarDesviacionUseCase = validarDesviacionUseCase;
        this.abrirNegociacionUseCase = abrirNegociacionUseCase;
        this.registrarContraofertaUseCase = registrarContraofertaUseCase;
        this.aceptarAbsorcionUseCase = aceptarAbsorcionUseCase;
        this.reabrirNegociacionUseCase = reabrirNegociacionUseCase;
        this.resolverWorkflowDisputaUseCase = resolverWorkflowDisputaUseCase;
        this.buscarDisputaComercialUseCase = buscarDisputaComercialUseCase;
    }

    // Notificación de discrepancia recibida - abre la disputa desde la excepción de Fase 01
    @PostMapping
    public ResponseEntity<?> abrir(@RequestBody AbrirDisputaComercialRequest request) {
        try {
            DisputaComercialResponse disputa = abrirDisputaComercialUseCase.ejecutar(request);
            return new ResponseEntity<>(disputa, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.1.1 - Extracción y Cotejo de Datos de Facturación vs. Acuerdos
    @PostMapping("/{id}/cotejar")
    public ResponseEntity<?> cotejar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cotejarFacturaContratoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.1.2 - Cuantificación del Impacto Financiero de la Desviación
    @PostMapping("/{id}/cuantificar")
    public ResponseEntity<?> cuantificar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cuantificarImpactoFinancieroUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.1.3 - Revisión y Validación de la Desviación
    @PostMapping("/{id}/validar-desviacion")
    public ResponseEntity<?> validarDesviacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(validarDesviacionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.1 - Apertura de Disputa con Ejecutivo de Droguería (nueva ronda)
    @PostMapping("/{id}/abrir-negociacion")
    public ResponseEntity<?> abrirNegociacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(abrirNegociacionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.1 (cont.) - el proveedor envía su contrapropuesta
    @PostMapping("/{id}/contraoferta")
    public ResponseEntity<?> registrarContraoferta(@PathVariable Long id,
                                                    @RequestBody RegistrarContraofertaRequest request) {
        try {
            return ResponseEntity.ok(registrarContraofertaUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.2 - ¿Se Absorbe la Diferencia Comercial? — Sí
    @PostMapping("/{id}/aceptar-absorcion")
    public ResponseEntity<?> aceptarAbsorcion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aceptarAbsorcionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.2.2 - ¿Se Absorbe la Diferencia Comercial? — No, reabrir
    @PostMapping("/{id}/reabrir-negociacion")
    public ResponseEntity<?> reabrirNegociacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reabrirNegociacionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2.3.1 - Registrar Resolución en Workflow del ERP
    @PostMapping("/{id}/resolver-workflow")
    public ResponseEntity<?> resolverWorkflow(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(resolverWorkflowDisputaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisputaComercialResponse> obtenerPorId(@PathVariable Long id) {
        return buscarDisputaComercialUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DisputaComercialResponse>> obtenerTodas(
            @RequestParam(required = false) Long excepcionFacturacionId) {
        if (excepcionFacturacionId != null) {
            return ResponseEntity.ok(buscarDisputaComercialUseCase.porExcepcionFacturacion(excepcionFacturacionId));
        }
        return ResponseEntity.ok(buscarDisputaComercialUseCase.todas());
    }
}
