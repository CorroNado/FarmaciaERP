package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CalcularPlanillaRequest;
import FarmaciaERP.Application.DTOs.Request.GuardarPlanillaRequest;
import FarmaciaERP.Application.DTOs.Response.PlanillaResponse;
import FarmaciaERP.Application.DTOs.Response.PlanillaResumenResponse;
import FarmaciaERP.Application.UseCases.BuscarPlanillaUseCase;
import FarmaciaERP.Application.UseCases.CalcularPlanillaUseCase;
import FarmaciaERP.Application.UseCases.EliminarPlanillaUseCase;
import FarmaciaERP.Application.UseCases.GuardarPlanillaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RRHH.03 - Nómina / Planilla: calcula el sueldo neto mensual de cada
 * colaborador activo (Contratación) a partir de sus registros de
 * asistencia (Control de Asistencia) — horas extras, faltas, tardanzas,
 * bonos automáticos y descuentos legales — y permite guardar la planilla
 * calculada como histórico mensual (mes/año único).
 */
@RestController
@RequestMapping("/api/rrhh/planillas")
public class PlanillaController {

    private final CalcularPlanillaUseCase calcularPlanillaUseCase;
    private final GuardarPlanillaUseCase guardarPlanillaUseCase;
    private final BuscarPlanillaUseCase buscarPlanillaUseCase;
    private final EliminarPlanillaUseCase eliminarPlanillaUseCase;

    public PlanillaController(CalcularPlanillaUseCase calcularPlanillaUseCase,
                               GuardarPlanillaUseCase guardarPlanillaUseCase,
                               BuscarPlanillaUseCase buscarPlanillaUseCase,
                               EliminarPlanillaUseCase eliminarPlanillaUseCase) {
        this.calcularPlanillaUseCase = calcularPlanillaUseCase;
        this.guardarPlanillaUseCase = guardarPlanillaUseCase;
        this.buscarPlanillaUseCase = buscarPlanillaUseCase;
        this.eliminarPlanillaUseCase = eliminarPlanillaUseCase;
    }

    // Calcular Planilla - previsualización en memoria, no persiste
    @PostMapping("/calcular")
    public ResponseEntity<?> calcular(@RequestBody CalcularPlanillaRequest request) {
        try {
            return ResponseEntity.ok(calcularPlanillaUseCase.ejecutar(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Guardar Planilla - persiste el mes/año (pide confirmarSobrescritura si ya existe)
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody GuardarPlanillaRequest request) {
        try {
            PlanillaResponse planilla = guardarPlanillaUseCase.ejecutar(request);
            return new ResponseEntity<>(planilla, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Planillas Guardadas - historial
    @GetMapping
    public ResponseEntity<List<PlanillaResumenResponse>> historial() {
        return ResponseEntity.ok(buscarPlanillaUseCase.historial());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanillaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarPlanillaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cargar Planilla Guardada - por mes/año
    @GetMapping("/buscar")
    public ResponseEntity<PlanillaResponse> obtenerPorMesYAnio(@RequestParam int mes, @RequestParam int anio) {
        return buscarPlanillaUseCase.porMesYAnio(mes, anio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar Planilla Guardada
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            eliminarPlanillaUseCase.ejecutar(id);
            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
