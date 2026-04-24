package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.UseCases.ActualizarPacienteUseCase;
import FarmaciaERP.Application.UseCases.BuscarPacienteUseCase;
import FarmaciaERP.Application.UseCases.CrearPacienteUseCase;
import FarmaciaERP.Application.UseCases.EliminarPacienteUseCase;
import FarmaciaERP.Domain.Entities.Paciente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final CrearPacienteUseCase crearPacienteUseCase;
    private final ActualizarPacienteUseCase actualizarPacienteUseCase;
    private final EliminarPacienteUseCase eliminarPacienteUseCase;
    private final BuscarPacienteUseCase buscarPacienteUseCase;

    public PacienteController(
            CrearPacienteUseCase crearPacienteUseCase,
            ActualizarPacienteUseCase actualizarPacienteUseCase,
            EliminarPacienteUseCase eliminarPacienteUseCase,
            BuscarPacienteUseCase buscarPacienteUseCase) {
        this.crearPacienteUseCase = crearPacienteUseCase;
        this.actualizarPacienteUseCase = actualizarPacienteUseCase;
        this.eliminarPacienteUseCase = eliminarPacienteUseCase;
        this.buscarPacienteUseCase = buscarPacienteUseCase;
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        try {
            Paciente nuevoPaciente = crearPacienteUseCase.ejecutar(paciente);
            return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable int id, @RequestBody Paciente paciente) {
        try {
            Paciente pacienteActualizado = actualizarPacienteUseCase.ejecutar(id, paciente);
            return ResponseEntity.ok(pacienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        try {
            eliminarPacienteUseCase.ejecutar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable int id) {
        return buscarPacienteUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> obtenerTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) TipoSeguro tipoSeguro) {

        if (dni != null) {
            return buscarPacienteUseCase.porDocumento(dni)
                    .map(paciente -> ResponseEntity.ok(List.of(paciente)))
                    .orElse(ResponseEntity.notFound().build());
        }

        if (nombre != null) {
            return ResponseEntity.ok(buscarPacienteUseCase.porNombre(nombre));
        }

        if (tipoSeguro != null) {
            return ResponseEntity.ok(buscarPacienteUseCase.porTipoSeguro(tipoSeguro));
        }

        return ResponseEntity.ok(buscarPacienteUseCase.todos());
    }
}
