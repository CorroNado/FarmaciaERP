package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.EditarRegistroAsistenciaRequest;
import FarmaciaERP.Application.DTOs.Request.EliminarRegistroAsistenciaRequest;
import FarmaciaERP.Application.DTOs.Request.JustificarInasistenciaRequest;
import FarmaciaERP.Application.DTOs.Request.ProgramarAsistenciaRequest;
import FarmaciaERP.Application.DTOs.Response.AsistenciaAuditLogResponse;
import FarmaciaERP.Application.DTOs.Response.RegistroAsistenciaResponse;
import FarmaciaERP.Application.UseCases.BuscarRegistroAsistenciaUseCase;
import FarmaciaERP.Application.UseCases.EditarRegistroAsistenciaUseCase;
import FarmaciaERP.Application.UseCases.EliminarRegistroAsistenciaUseCase;
import FarmaciaERP.Application.UseCases.JustificarInasistenciaUseCase;
import FarmaciaERP.Application.UseCases.ListarAuditoriaAsistenciaUseCase;
import FarmaciaERP.Application.UseCases.MarcarEntradaUseCase;
import FarmaciaERP.Application.UseCases.MarcarSalidaUseCase;
import FarmaciaERP.Application.UseCases.ProgramarAsistenciaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * RRHH.02 - Control de Asistencia: espejo del módulo "Monitoreo de
 * Asistencia en Tiempo Real" del prototipo (asistencia.js): programación de
 * turnos, marcación de entrada/salida con cálculo de horas extras,
 * justificación de inasistencias y edición/eliminación auditadas.
 */
@RestController
@RequestMapping("/api/rrhh/asistencias")
public class RegistroAsistenciaController {

    private final ProgramarAsistenciaUseCase programarAsistenciaUseCase;
    private final MarcarEntradaUseCase marcarEntradaUseCase;
    private final MarcarSalidaUseCase marcarSalidaUseCase;
    private final JustificarInasistenciaUseCase justificarInasistenciaUseCase;
    private final EditarRegistroAsistenciaUseCase editarRegistroAsistenciaUseCase;
    private final EliminarRegistroAsistenciaUseCase eliminarRegistroAsistenciaUseCase;
    private final BuscarRegistroAsistenciaUseCase buscarRegistroAsistenciaUseCase;
    private final ListarAuditoriaAsistenciaUseCase listarAuditoriaAsistenciaUseCase;

    public RegistroAsistenciaController(ProgramarAsistenciaUseCase programarAsistenciaUseCase,
                                         MarcarEntradaUseCase marcarEntradaUseCase,
                                         MarcarSalidaUseCase marcarSalidaUseCase,
                                         JustificarInasistenciaUseCase justificarInasistenciaUseCase,
                                         EditarRegistroAsistenciaUseCase editarRegistroAsistenciaUseCase,
                                         EliminarRegistroAsistenciaUseCase eliminarRegistroAsistenciaUseCase,
                                         BuscarRegistroAsistenciaUseCase buscarRegistroAsistenciaUseCase,
                                         ListarAuditoriaAsistenciaUseCase listarAuditoriaAsistenciaUseCase) {
        this.programarAsistenciaUseCase = programarAsistenciaUseCase;
        this.marcarEntradaUseCase = marcarEntradaUseCase;
        this.marcarSalidaUseCase = marcarSalidaUseCase;
        this.justificarInasistenciaUseCase = justificarInasistenciaUseCase;
        this.editarRegistroAsistenciaUseCase = editarRegistroAsistenciaUseCase;
        this.eliminarRegistroAsistenciaUseCase = eliminarRegistroAsistenciaUseCase;
        this.buscarRegistroAsistenciaUseCase = buscarRegistroAsistenciaUseCase;
        this.listarAuditoriaAsistenciaUseCase = listarAuditoriaAsistenciaUseCase;
    }

    // + Registrar Entrada (crea el turno en estado "Programado")
    @PostMapping
    public ResponseEntity<?> programar(@RequestBody ProgramarAsistenciaRequest request) {
        try {
            RegistroAsistenciaResponse registro = programarAsistenciaUseCase.ejecutar(request);
            return new ResponseEntity<>(registro, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Entrada
    @PostMapping("/{id}/entrada")
    public ResponseEntity<?> marcarEntrada(@PathVariable Long id,
                                            @RequestParam(defaultValue = "Sistema") String usuario) {
        try {
            return ResponseEntity.ok(marcarEntradaUseCase.ejecutar(id, usuario));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🚪 Salida
    @PostMapping("/{id}/salida")
    public ResponseEntity<?> marcarSalida(@PathVariable Long id,
                                           @RequestParam(defaultValue = "Sistema") String usuario) {
        try {
            return ResponseEntity.ok(marcarSalidaUseCase.ejecutar(id, usuario));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📄 Justificación de Inasistencia
    @PostMapping("/{id}/justificar")
    public ResponseEntity<?> justificar(@PathVariable Long id, @RequestBody JustificarInasistenciaRequest request) {
        try {
            return ResponseEntity.ok(justificarInasistenciaUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Auditoría de Registro — Editar
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody EditarRegistroAsistenciaRequest request) {
        try {
            return ResponseEntity.ok(editarRegistroAsistenciaUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Auditoría de Registro — Borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestBody EliminarRegistroAsistenciaRequest request) {
        try {
            eliminarRegistroAsistenciaUseCase.ejecutar(id, request);
            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAsistenciaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarRegistroAsistenciaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RegistroAsistenciaResponse>> obtenerTodos(
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) Long empleadoId,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        if (empleadoId != null && mes != null && anio != null) {
            return ResponseEntity.ok(buscarRegistroAsistenciaUseCase.porEmpleadoYMes(empleadoId, mes, anio));
        }
        if (empleadoId != null) {
            return ResponseEntity.ok(buscarRegistroAsistenciaUseCase.porEmpleado(empleadoId));
        }
        if (fecha != null) {
            return ResponseEntity.ok(buscarRegistroAsistenciaUseCase.porFecha(fecha));
        }
        return ResponseEntity.ok(buscarRegistroAsistenciaUseCase.todos());
    }

    @GetMapping("/auditoria")
    public ResponseEntity<List<AsistenciaAuditLogResponse>> obtenerAuditoria(
            @RequestParam(required = false) String codigoEmpleado) {
        if (codigoEmpleado != null && !codigoEmpleado.isBlank()) {
            return ResponseEntity.ok(listarAuditoriaAsistenciaUseCase.porCodigoEmpleado(codigoEmpleado));
        }
        return ResponseEntity.ok(listarAuditoriaAsistenciaUseCase.todos());
    }
}
