package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearConvenioRequest;
import FarmaciaERP.Application.DTOs.Response.ConvenioResponse;
import FarmaciaERP.Application.UseCases.BuscarConvenioUseCase;
import FarmaciaERP.Application.UseCases.CrearConvenioUseCase;
import FarmaciaERP.Domain.Enums.EstadoConvenio;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistica/convenios")
public class ConvenioController {

    private final CrearConvenioUseCase crearConvenioUseCase;
    private final BuscarConvenioUseCase buscarConvenioUseCase;

    public ConvenioController(CrearConvenioUseCase crearConvenioUseCase,
                               BuscarConvenioUseCase buscarConvenioUseCase) {
        this.crearConvenioUseCase = crearConvenioUseCase;
        this.buscarConvenioUseCase = buscarConvenioUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearConvenioRequest request) {
        try {
            ConvenioResponse creado = crearConvenioUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvenioResponse> obtenerPorId(@PathVariable Long id) {
        return buscarConvenioUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ConvenioResponse>> obtenerTodos(
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) EstadoConvenio estado) {
        if (proveedorId != null) {
            return ResponseEntity.ok(buscarConvenioUseCase.porProveedor(proveedorId));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarConvenioUseCase.porEstado(estado));
        }
        return ResponseEntity.ok(buscarConvenioUseCase.todos());
    }
}
