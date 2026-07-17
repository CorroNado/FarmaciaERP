package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.*;
import FarmaciaERP.Application.DTOs.Response.EmpleadoAuditLogResponse;
import FarmaciaERP.Application.DTOs.Response.EmpleadoResponse;
import FarmaciaERP.Application.UseCases.*;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RRHH.01 - Contratación: registro de colaboradores y datos laborales.
 * Espejo del módulo "Gestión de Contratación" del prototipo, incluida la
 * lógica de bajas inteligentes (inmediata / programada) según si el
 * colaborador tiene turnos activos.
 */
@RestController
@RequestMapping("/api/rrhh/empleados")
public class EmpleadoController {

    private final CrearEmpleadoUseCase crearEmpleadoUseCase;
    private final ActualizarEmpleadoUseCase actualizarEmpleadoUseCase;
    private final EliminarEmpleadoUseCase eliminarEmpleadoUseCase;
    private final BuscarEmpleadoUseCase buscarEmpleadoUseCase;
    private final ReactivarEmpleadoUseCase reactivarEmpleadoUseCase;
    private final DarBajaSinTurnosActivosUseCase darBajaSinTurnosActivosUseCase;
    private final DarBajaInmediataUseCase darBajaInmediataUseCase;
    private final ProgramarBajaUseCase programarBajaUseCase;
    private final EjecutarBajasProgramadasUseCase ejecutarBajasProgramadasUseCase;
    private final ListarAuditoriaEmpleadosUseCase listarAuditoriaEmpleadosUseCase;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleadoUseCase,
                               ActualizarEmpleadoUseCase actualizarEmpleadoUseCase,
                               EliminarEmpleadoUseCase eliminarEmpleadoUseCase,
                               BuscarEmpleadoUseCase buscarEmpleadoUseCase,
                               ReactivarEmpleadoUseCase reactivarEmpleadoUseCase,
                               DarBajaSinTurnosActivosUseCase darBajaSinTurnosActivosUseCase,
                               DarBajaInmediataUseCase darBajaInmediataUseCase,
                               ProgramarBajaUseCase programarBajaUseCase,
                               EjecutarBajasProgramadasUseCase ejecutarBajasProgramadasUseCase,
                               ListarAuditoriaEmpleadosUseCase listarAuditoriaEmpleadosUseCase) {
        this.crearEmpleadoUseCase = crearEmpleadoUseCase;
        this.actualizarEmpleadoUseCase = actualizarEmpleadoUseCase;
        this.eliminarEmpleadoUseCase = eliminarEmpleadoUseCase;
        this.buscarEmpleadoUseCase = buscarEmpleadoUseCase;
        this.reactivarEmpleadoUseCase = reactivarEmpleadoUseCase;
        this.darBajaSinTurnosActivosUseCase = darBajaSinTurnosActivosUseCase;
        this.darBajaInmediataUseCase = darBajaInmediataUseCase;
        this.programarBajaUseCase = programarBajaUseCase;
        this.ejecutarBajasProgramadasUseCase = ejecutarBajasProgramadasUseCase;
        this.listarAuditoriaEmpleadosUseCase = listarAuditoriaEmpleadosUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam(defaultValue = "Sistema") String usuario,
                                    @RequestBody CrearEmpleadoRequest request) {
        try {
            EmpleadoResponse creado = crearEmpleadoUseCase.ejecutar(usuario, request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                         @RequestParam(defaultValue = "Sistema") String usuario,
                                         @RequestBody ActualizarEmpleadoRequest request) {
        try {
            return ResponseEntity.ok(actualizarEmpleadoUseCase.ejecutar(id, usuario, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                       @RequestParam(defaultValue = "Sistema") String usuario) {
        try {
            eliminarEmpleadoUseCase.ejecutar(id, usuario);
            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> obtenerPorId(@PathVariable Long id) {
        return buscarEmpleadoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> obtenerTodos(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) EstadoEmpleado estado) {
        if (texto != null && !texto.isBlank()) {
            return ResponseEntity.ok(buscarEmpleadoUseCase.buscar(texto));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarEmpleadoUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarEmpleadoUseCase.todos());
    }

    // --- Cambios de estado: lógica de bajas inteligentes ---

    @PostMapping("/{id}/reactivar")
    public ResponseEntity<?> reactivar(@PathVariable Long id, @RequestBody CambiarEstadoEmpleadoRequest request) {
        try {
            return ResponseEntity.ok(reactivarEmpleadoUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Caso 1: el colaborador no tiene turnos activos → baja directa. */
    @PostMapping("/{id}/baja-sin-turnos")
    public ResponseEntity<?> darBajaSinTurnosActivos(@PathVariable Long id, @RequestBody CambiarEstadoEmpleadoRequest request) {
        try {
            return ResponseEntity.ok(darBajaSinTurnosActivosUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Caso 2.a: el colaborador tiene turnos activos, pero se decide dar de baja de inmediato. */
    @PostMapping("/{id}/baja-inmediata")
    public ResponseEntity<?> darBajaInmediata(@PathVariable Long id, @RequestBody BajaInmediataRequest request) {
        try {
            return ResponseEntity.ok(darBajaInmediataUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Caso 2.b: se programa la baja para el término del último turno activo. */
    @PostMapping("/{id}/baja-programada")
    public ResponseEntity<?> programarBaja(@PathVariable Long id, @RequestBody ProgramarBajaRequest request) {
        try {
            return ResponseEntity.ok(programarBajaUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Disparo manual del monitoreo de bajas programadas (además del job automático cada minuto). */
    @PostMapping("/bajas-programadas/ejecutar")
    public ResponseEntity<Integer> ejecutarBajasProgramadas() {
        return ResponseEntity.ok(ejecutarBajasProgramadasUseCase.ejecutar());
    }

    // --- Auditoría ---

    @GetMapping("/auditoria")
    public ResponseEntity<List<EmpleadoAuditLogResponse>> auditoria(@RequestParam(required = false) String codigo) {
        if (codigo != null && !codigo.isBlank()) {
            return ResponseEntity.ok(listarAuditoriaEmpleadosUseCase.porCodigoEmpleado(codigo));
        }
        return ResponseEntity.ok(listarAuditoriaEmpleadosUseCase.todos());
    }
}
