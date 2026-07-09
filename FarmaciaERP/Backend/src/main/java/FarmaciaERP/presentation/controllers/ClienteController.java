package FarmaciaERP.presentation.controllers;

import FarmaciaERP.application.dto.Request.CrearClienteRequest;
import FarmaciaERP.application.dto.Response.CrearClienteResponse;
import FarmaciaERP.application.usecases.ActualizarClienteUseCase;
import FarmaciaERP.application.usecases.BuscarClienteUseCase;
import FarmaciaERP.application.usecases.CrearClienteUseCase;
import FarmaciaERP.application.usecases.EliminarClienteUseCase;
import FarmaciaERP.domain.entities.Cliente;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.domain.valueObjects.Dni;
import FarmaciaERP.domain.valueObjects.FullName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
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
    public ResponseEntity<?> crear(@RequestBody CrearClienteRequest request) {
        try {
            System.out.println("POST CLIENTE EJECUTANDO");
            CrearClienteResponse nuevoPaciente = crearClienteUseCase.ejecutar(request);
            return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = actualizarClienteUseCase.ejecutar(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            eliminarClienteUseCase.ejecutar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return buscarClienteUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos(
            @RequestParam(required = false) FullName nombres,
            @RequestParam(required = false) Dni dni,
            @RequestParam(required = false) InsuranceType InsuranceType) {
        if (dni != null) {
            return buscarClienteUseCase.porDocumento(dni)
                    .map(paciente -> ResponseEntity.ok(List.of(paciente)))
                    .orElse(ResponseEntity.notFound().build());
        }
        if (nombres != null) {
            return ResponseEntity.ok(buscarClienteUseCase.porNombre(nombres));
        }

        if (InsuranceType != null) {
            return ResponseEntity.ok(buscarClienteUseCase.porInsuranceType(InsuranceType));
        }
        return ResponseEntity.ok(buscarClienteUseCase.todos());
    }
}
