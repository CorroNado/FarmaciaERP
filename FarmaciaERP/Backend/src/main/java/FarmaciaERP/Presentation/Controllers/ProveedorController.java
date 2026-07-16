package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.ActualizarProveedorRequest;
import FarmaciaERP.Application.DTOs.Request.CrearProveedorRequest;
import FarmaciaERP.Application.DTOs.Response.ProveedorResponse;
import FarmaciaERP.Application.UseCases.ActualizarProveedorUseCase;
import FarmaciaERP.Application.UseCases.BuscarProveedorUseCase;
import FarmaciaERP.Application.UseCases.CrearProveedorUseCase;
import FarmaciaERP.Application.UseCases.EliminarProveedorUseCase;
import FarmaciaERP.Domain.Enums.EstadoProveedor;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistica/proveedores")
public class ProveedorController {

    private final CrearProveedorUseCase crearProveedorUseCase;
    private final ActualizarProveedorUseCase actualizarProveedorUseCase;
    private final EliminarProveedorUseCase eliminarProveedorUseCase;
    private final BuscarProveedorUseCase buscarProveedorUseCase;

    public ProveedorController(CrearProveedorUseCase crearProveedorUseCase,
                                ActualizarProveedorUseCase actualizarProveedorUseCase,
                                EliminarProveedorUseCase eliminarProveedorUseCase,
                                BuscarProveedorUseCase buscarProveedorUseCase) {
        this.crearProveedorUseCase = crearProveedorUseCase;
        this.actualizarProveedorUseCase = actualizarProveedorUseCase;
        this.eliminarProveedorUseCase = eliminarProveedorUseCase;
        this.buscarProveedorUseCase = buscarProveedorUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearProveedorRequest request) {
        try {
            ProveedorResponse creado = crearProveedorUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ActualizarProveedorRequest request) {
        try {
            return ResponseEntity.ok(actualizarProveedorUseCase.ejecutar(id, request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            eliminarProveedorUseCase.ejecutar(id);
            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> obtenerPorId(@PathVariable Long id) {
        return buscarProveedorUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> obtenerTodos(
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) EstadoProveedor estado) {
        if (razonSocial != null) {
            return ResponseEntity.ok(buscarProveedorUseCase.porRazonSocial(razonSocial));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarProveedorUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarProveedorUseCase.todos());
    }
}
