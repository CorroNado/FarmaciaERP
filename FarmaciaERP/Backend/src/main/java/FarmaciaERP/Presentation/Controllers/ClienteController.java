package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearPacienteRequest;
import FarmaciaERP.Application.DTOs.Response.CrearPacienteResponse;
import FarmaciaERP.Application.UseCases.ActualizarClienteUseCase;
import FarmaciaERP.Application.UseCases.BuscarClienteUseCase;
import FarmaciaERP.Application.UseCases.CrearClienteUseCase;
import FarmaciaERP.Application.UseCases.EliminarClienteUseCase;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class ClienteController {

    private final CrearClienteUseCase crearClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;
    private final BuscarClienteUseCase buscarClienteUseCase;

    public ClienteController(
            CrearClienteUseCase crearClienteUseCase,
            ActualizarClienteUseCase actualizarClienteUseCase,
            EliminarClienteUseCase eliminarClienteUseCase,
            BuscarClienteUseCase buscarClienteUseCase) {
        this.crearClienteUseCase = crearClienteUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
        this.eliminarClienteUseCase = eliminarClienteUseCase;
        this.buscarClienteUseCase = buscarClienteUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearPacienteRequest request) {
        try {
            CrearPacienteResponse nuevoPaciente = crearClienteUseCase.ejecutar(request);
            return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable int id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = actualizarClienteUseCase.ejecutar(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        try {
            eliminarClienteUseCase.ejecutar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable int id) {
        return buscarClienteUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos(
            @RequestParam(required = false) FullName nombres,
            @RequestParam(required = false) Dni dni,
            @RequestParam(required = false) TipoSeguro tipoSeguro) {

        if (dni != null) {
            return buscarClienteUseCase.porDocumento(dni)
                    .map(paciente -> ResponseEntity.ok(List.of(paciente)))
                    .orElse(ResponseEntity.notFound().build());
        }

        if (nombres != null) {
            return ResponseEntity.ok(buscarClienteUseCase.porNombre(nombres));
        }

        if (tipoSeguro != null) {
            return ResponseEntity.ok(buscarClienteUseCase.porTipoSeguro(tipoSeguro));
        }

        return ResponseEntity.ok(buscarClienteUseCase.todos());
    }
}
