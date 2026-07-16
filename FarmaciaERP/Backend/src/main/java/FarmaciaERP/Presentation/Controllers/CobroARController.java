package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.ConciliarComisionesRetencionesRequest;
import FarmaciaERP.Application.DTOs.Request.InterpretarArchivoTransferenciaRequest;
import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Application.UseCases.BuscarCobroARUseCase;
import FarmaciaERP.Application.UseCases.ConciliarComisionesRetencionesUseCase;
import FarmaciaERP.Application.UseCases.IngresarAjusteContableCobroUseCase;
import FarmaciaERP.Application.UseCases.InterpretarArchivoTransferenciaUseCase;
import FarmaciaERP.Application.UseCases.RegistrarIngresoImputacionUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FI-AR · Fase 05 — Procesamiento de Cobros e Imputación Bancaria
 * (RN-AR5-01). El archivo de transferencia bancaria recibido de la
 * entidad recaudadora se interpreta y se concilia contra las
 * comisiones de tarjeta y retenciones esperadas; ante un descuadre se
 * ingresa el ajuste contable por diferencia, y una vez cuadrado el
 * Sistema ERP registra el ingreso de dinero e imputación en la cuenta
 * del cliente, habilitando la Fase 06.
 */
@RestController
@RequestMapping("/api/fi-ar/cobros")
public class CobroARController {

    private final InterpretarArchivoTransferenciaUseCase interpretarArchivoTransferenciaUseCase;
    private final ConciliarComisionesRetencionesUseCase conciliarComisionesRetencionesUseCase;
    private final IngresarAjusteContableCobroUseCase ingresarAjusteContableCobroUseCase;
    private final RegistrarIngresoImputacionUseCase registrarIngresoImputacionUseCase;
    private final BuscarCobroARUseCase buscarCobroARUseCase;

    public CobroARController(InterpretarArchivoTransferenciaUseCase interpretarArchivoTransferenciaUseCase,
                              ConciliarComisionesRetencionesUseCase conciliarComisionesRetencionesUseCase,
                              IngresarAjusteContableCobroUseCase ingresarAjusteContableCobroUseCase,
                              RegistrarIngresoImputacionUseCase registrarIngresoImputacionUseCase,
                              BuscarCobroARUseCase buscarCobroARUseCase) {
        this.interpretarArchivoTransferenciaUseCase = interpretarArchivoTransferenciaUseCase;
        this.conciliarComisionesRetencionesUseCase = conciliarComisionesRetencionesUseCase;
        this.ingresarAjusteContableCobroUseCase = ingresarAjusteContableCobroUseCase;
        this.registrarIngresoImputacionUseCase = registrarIngresoImputacionUseCase;
        this.buscarCobroARUseCase = buscarCobroARUseCase;
    }

    // 5.1 - Interpretar Archivo de Transferencia bancaria (Sistema ERP)
    @PostMapping
    public ResponseEntity<?> interpretar(@RequestBody InterpretarArchivoTransferenciaRequest request) {
        try {
            CobroARResponse cobro = interpretarArchivoTransferenciaUseCase.ejecutar(request);
            return new ResponseEntity<>(cobro, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.2 - Conciliar Comisiones de Tarjetas y Retenciones (Analista AR)
    @PostMapping("/{id}/conciliar-comisiones")
    public ResponseEntity<?> conciliarComisiones(@PathVariable Long id,
                                                  @RequestBody ConciliarComisionesRetencionesRequest request) {
        try {
            return ResponseEntity.ok(conciliarComisionesRetencionesUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5.3 - Ingresar Ajuste Contable por Diferencia — solo si hubo descuadre
    @PostMapping("/{id}/ajuste-diferencia")
    public ResponseEntity<?> ingresarAjusteDiferencia(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ingresarAjusteContableCobroUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.1 - Registrar Ingreso de Dinero e Imputación en la cuenta del cliente (Sistema ERP)
    @PostMapping("/{id}/registrar-ingreso")
    public ResponseEntity<?> registrarIngreso(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(registrarIngresoImputacionUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CobroARResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCobroARUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosOPorLote(@RequestParam(required = false) Long contabilizacionARId) {
        if (contabilizacionARId != null) {
            return buscarCobroARUseCase.porContabilizacionAR(contabilizacionARId)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(buscarCobroARUseCase.todos());
    }

    // RN-AR5-01: indica si el lote ya puede continuar a la Fase 06 (ingreso registrado)
    @GetMapping("/puede-continuar-fase06")
    public ResponseEntity<Map<String, Boolean>> puedeContinuarFase06(@RequestParam Long contabilizacionARId) {
        boolean puede = buscarCobroARUseCase.puedeContinuarFase06(contabilizacionARId);
        return ResponseEntity.ok(Map.of("puedeContinuarFase06", puede));
    }
}
