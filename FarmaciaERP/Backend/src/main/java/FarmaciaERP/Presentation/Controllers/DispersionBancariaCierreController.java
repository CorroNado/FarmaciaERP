package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.IniciarDispersionBancariaRequest;
import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Application.UseCases.AplicarFirmaDigitalUseCase;
import FarmaciaERP.Application.UseCases.BuscarDispersionBancariaUseCase;
import FarmaciaERP.Application.UseCases.CompilarPropuestaPagoUseCase;
import FarmaciaERP.Application.UseCases.ConciliarCuentasPuenteUseCase;
import FarmaciaERP.Application.UseCases.CorregirErroresYReenviarLoteUseCase;
import FarmaciaERP.Application.UseCases.EjecutarTransferenciasBancariasUseCase;
import FarmaciaERP.Application.UseCases.GenerarArchivoBancarioUseCase;
import FarmaciaERP.Application.UseCases.ImportarExtractoBancarioUseCase;
import FarmaciaERP.Application.UseCases.IniciarDispersionBancariaUseCase;
import FarmaciaERP.Application.UseCases.ValidarPropuestaDuplicadosUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.06 - Fase 06: Dispersión Bancaria y Conciliación de Cierre
 * (RN-AP6-01 · SAP FI-AP). La propuesta de pago se valida contra
 * duplicados, se firma digitalmente y se transfiere a través de banca
 * empresa. El cierre concluye al importar el extracto bancario y
 * compensar la cuenta transitoria contra el saldo de proveedores,
 * extinguiendo la obligación y finalizando el ciclo FI-AP.
 */
@RestController
@RequestMapping("/api/fi-ap/dispersiones-bancarias")
public class DispersionBancariaCierreController {

    private final IniciarDispersionBancariaUseCase iniciarDispersionBancariaUseCase;
    private final CompilarPropuestaPagoUseCase compilarPropuestaPagoUseCase;
    private final ValidarPropuestaDuplicadosUseCase validarPropuestaDuplicadosUseCase;
    private final CorregirErroresYReenviarLoteUseCase corregirErroresYReenviarLoteUseCase;
    private final GenerarArchivoBancarioUseCase generarArchivoBancarioUseCase;
    private final AplicarFirmaDigitalUseCase aplicarFirmaDigitalUseCase;
    private final EjecutarTransferenciasBancariasUseCase ejecutarTransferenciasBancariasUseCase;
    private final ImportarExtractoBancarioUseCase importarExtractoBancarioUseCase;
    private final ConciliarCuentasPuenteUseCase conciliarCuentasPuenteUseCase;
    private final BuscarDispersionBancariaUseCase buscarDispersionBancariaUseCase;

    public DispersionBancariaCierreController(IniciarDispersionBancariaUseCase iniciarDispersionBancariaUseCase,
                                               CompilarPropuestaPagoUseCase compilarPropuestaPagoUseCase,
                                               ValidarPropuestaDuplicadosUseCase validarPropuestaDuplicadosUseCase,
                                               CorregirErroresYReenviarLoteUseCase corregirErroresYReenviarLoteUseCase,
                                               GenerarArchivoBancarioUseCase generarArchivoBancarioUseCase,
                                               AplicarFirmaDigitalUseCase aplicarFirmaDigitalUseCase,
                                               EjecutarTransferenciasBancariasUseCase ejecutarTransferenciasBancariasUseCase,
                                               ImportarExtractoBancarioUseCase importarExtractoBancarioUseCase,
                                               ConciliarCuentasPuenteUseCase conciliarCuentasPuenteUseCase,
                                               BuscarDispersionBancariaUseCase buscarDispersionBancariaUseCase) {
        this.iniciarDispersionBancariaUseCase = iniciarDispersionBancariaUseCase;
        this.compilarPropuestaPagoUseCase = compilarPropuestaPagoUseCase;
        this.validarPropuestaDuplicadosUseCase = validarPropuestaDuplicadosUseCase;
        this.corregirErroresYReenviarLoteUseCase = corregirErroresYReenviarLoteUseCase;
        this.generarArchivoBancarioUseCase = generarArchivoBancarioUseCase;
        this.aplicarFirmaDigitalUseCase = aplicarFirmaDigitalUseCase;
        this.ejecutarTransferenciasBancariasUseCase = ejecutarTransferenciasBancariasUseCase;
        this.importarExtractoBancarioUseCase = importarExtractoBancarioUseCase;
        this.conciliarCuentasPuenteUseCase = conciliarCuentasPuenteUseCase;
        this.buscarDispersionBancariaUseCase = buscarDispersionBancariaUseCase;
    }

    // Inicio de la dispersión - a partir de la propuesta de pago concluida en Fase 05
    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarDispersionBancariaRequest request) {
        try {
            DispersionBancariaCierreResponse dispersion = iniciarDispersionBancariaUseCase.ejecutar(request);
            return new ResponseEntity<>(dispersion, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.1 - Compilar Propuesta de Pago (F110) recibida desde la Fase 05
    @PostMapping("/{id}/compilar")
    public ResponseEntity<?> compilarPropuestaPago(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(compilarPropuestaPagoUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.2 - Validar Propuesta de Duplicados / Bloqueos
    @PostMapping("/{id}/validar")
    public ResponseEntity<?> validarPropuestaDuplicados(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(validarPropuestaDuplicadosUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.2 (cont.) - Corregir Errores y Reenviar Lote
    @PostMapping("/{id}/corregir-reenviar")
    public ResponseEntity<?> corregirErroresYReenviarLote(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(corregirErroresYReenviarLoteUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.3 - Generar Archivo Bancario Plano (IDoc)
    @PostMapping("/{id}/generar-archivo")
    public ResponseEntity<?> generarArchivoBancario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(generarArchivoBancarioUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.4 - Aplicar Firma Digital con Token Bancario
    @PostMapping("/{id}/firmar")
    public ResponseEntity<?> aplicarFirmaDigital(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aplicarFirmaDigitalUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.5 - Ejecutar Transferencias en Banca Empresa (Token)
    @PostMapping("/{id}/transferir")
    public ResponseEntity<?> ejecutarTransferenciasBancarias(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ejecutarTransferenciasBancariasUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.6 - Importar Extracto Bancario Digital del Día (FF.5)
    @PostMapping("/{id}/importar-extracto")
    public ResponseEntity<?> importarExtractoBancario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(importarExtractoBancarioUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6.7 - Conciliar Cuentas Puente Financieras y Compensar Cuenta Transitoria
    @PostMapping("/{id}/conciliar")
    public ResponseEntity<?> conciliarCuentasPuente(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(conciliarCuentasPuenteUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispersionBancariaCierreResponse> obtenerPorId(@PathVariable Long id) {
        return buscarDispersionBancariaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DispersionBancariaCierreResponse>> obtenerTodos() {
        return ResponseEntity.ok(buscarDispersionBancariaUseCase.todos());
    }
}
