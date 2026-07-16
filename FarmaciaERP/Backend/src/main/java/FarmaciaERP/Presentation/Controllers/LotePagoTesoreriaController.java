package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.IniciarLotePagoRequest;
import FarmaciaERP.Application.DTOs.Request.NegociarDescuentoProntoPagoRequest;
import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Application.UseCases.BuscarLotePagoUseCase;
import FarmaciaERP.Application.UseCases.CorregirLoteUseCase;
import FarmaciaERP.Application.UseCases.EjecutarPagosYConciliarUseCase;
import FarmaciaERP.Application.UseCases.IniciarLotePagoUseCase;
import FarmaciaERP.Application.UseCases.NegociarDescuentoProntoPagoUseCase;
import FarmaciaERP.Application.UseCases.PrepararLotePagosUseCase;
import FarmaciaERP.Application.UseCases.PriorizarProveedoresCriticosUseCase;
import FarmaciaERP.Application.UseCases.SometerLoteAComiteUseCase;
import FarmaciaERP.Application.UseCases.VerificarFondosYValidarLoteUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FI-AP.04 - Fase 04: Estrategia de Tesorería y Riesgo Sanitario. Se
 * priorizan los proveedores de medicamentos críticos, se negocia el
 * descuento por pronto pago y se prepara el lote de pagos (F110 SAP),
 * sujeto a verificación de fondos y a la aprobación del Comité Semanal de
 * Tesorería antes de ejecutar y conciliar los pagos.
 */
@RestController
@RequestMapping("/api/fi-ap/lotes-pago")
public class LotePagoTesoreriaController {

    private final IniciarLotePagoUseCase iniciarLotePagoUseCase;
    private final PriorizarProveedoresCriticosUseCase priorizarProveedoresCriticosUseCase;
    private final NegociarDescuentoProntoPagoUseCase negociarDescuentoProntoPagoUseCase;
    private final PrepararLotePagosUseCase prepararLotePagosUseCase;
    private final VerificarFondosYValidarLoteUseCase verificarFondosYValidarLoteUseCase;
    private final SometerLoteAComiteUseCase someterLoteAComiteUseCase;
    private final CorregirLoteUseCase corregirLoteUseCase;
    private final EjecutarPagosYConciliarUseCase ejecutarPagosYConciliarUseCase;
    private final BuscarLotePagoUseCase buscarLotePagoUseCase;

    public LotePagoTesoreriaController(IniciarLotePagoUseCase iniciarLotePagoUseCase,
                                        PriorizarProveedoresCriticosUseCase priorizarProveedoresCriticosUseCase,
                                        NegociarDescuentoProntoPagoUseCase negociarDescuentoProntoPagoUseCase,
                                        PrepararLotePagosUseCase prepararLotePagosUseCase,
                                        VerificarFondosYValidarLoteUseCase verificarFondosYValidarLoteUseCase,
                                        SometerLoteAComiteUseCase someterLoteAComiteUseCase,
                                        CorregirLoteUseCase corregirLoteUseCase,
                                        EjecutarPagosYConciliarUseCase ejecutarPagosYConciliarUseCase,
                                        BuscarLotePagoUseCase buscarLotePagoUseCase) {
        this.iniciarLotePagoUseCase = iniciarLotePagoUseCase;
        this.priorizarProveedoresCriticosUseCase = priorizarProveedoresCriticosUseCase;
        this.negociarDescuentoProntoPagoUseCase = negociarDescuentoProntoPagoUseCase;
        this.prepararLotePagosUseCase = prepararLotePagosUseCase;
        this.verificarFondosYValidarLoteUseCase = verificarFondosYValidarLoteUseCase;
        this.someterLoteAComiteUseCase = someterLoteAComiteUseCase;
        this.corregirLoteUseCase = corregirLoteUseCase;
        this.ejecutarPagosYConciliarUseCase = ejecutarPagosYConciliarUseCase;
        this.buscarLotePagoUseCase = buscarLotePagoUseCase;
    }

    // Inicio del armado del lote - a partir de los ajustes contables regularizados en Fase 03
    @PostMapping
    public ResponseEntity<?> iniciar(@RequestBody IniciarLotePagoRequest request) {
        try {
            LotePagoTesoreriaResponse lote = iniciarLotePagoUseCase.ejecutar(request);
            return new ResponseEntity<>(lote, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.1 - Priorizar Proveedores Críticos de Medicamentos
    @PostMapping("/{id}/priorizar")
    public ResponseEntity<?> priorizarProveedoresCriticos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(priorizarProveedoresCriticosUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.2 - Negociar Descuento por Pronto Pago
    @PostMapping("/{id}/negociar-descuento")
    public ResponseEntity<?> negociarDescuentoProntoPago(@PathVariable Long id,
                                                           @RequestBody NegociarDescuentoProntoPagoRequest request) {
        try {
            return ResponseEntity.ok(negociarDescuentoProntoPagoUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.3 - Preparar Lote de Pagos (F110 SAP)
    @PostMapping("/{id}/preparar")
    public ResponseEntity<?> prepararLotePagos(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(prepararLotePagosUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.4 - Verificar Fondos y Validar Lote
    @PostMapping("/{id}/verificar-fondos")
    public ResponseEntity<?> verificarFondosYValidarLote(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(verificarFondosYValidarLoteUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.4 (cont.) - ¿Lote Aprobado? Someter al Comité Semanal de Tesorería
    @PostMapping("/{id}/someter-comite")
    public ResponseEntity<?> someterAComite(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(someterLoteAComiteUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.4 (cont.) - Corregir Lote según Observaciones del Comité
    @PostMapping("/{id}/corregir")
    public ResponseEntity<?> corregirLote(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(corregirLoteUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4.5 - Ejecutar Pagos y Conciliar en SAP FI-AP
    @PostMapping("/{id}/ejecutar-conciliar")
    public ResponseEntity<?> ejecutarPagosYConciliar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ejecutarPagosYConciliarUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotePagoTesoreriaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarLotePagoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<LotePagoTesoreriaResponse>> obtenerTodos() {
        return ResponseEntity.ok(buscarLotePagoUseCase.todos());
    }
}
