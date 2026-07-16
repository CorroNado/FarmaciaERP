package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearDevolucionRequest;
import FarmaciaERP.Application.DTOs.Response.DevolucionResponse;
import FarmaciaERP.Application.UseCases.BuscarDevolucionUseCase;
import FarmaciaERP.Application.UseCases.CrearDevolucionUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devolucion")
public class DevolucionController {

    private final CrearDevolucionUseCase crearDevolucionUseCase;
    private final BuscarDevolucionUseCase buscarDevolucionUseCase;

    public DevolucionController(CrearDevolucionUseCase crearDevolucionUseCase,
                                 BuscarDevolucionUseCase buscarDevolucionUseCase) {
        this.crearDevolucionUseCase = crearDevolucionUseCase;
        this.buscarDevolucionUseCase = buscarDevolucionUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearDevolucionRequest request) {
        try {
            DevolucionResponse devolucion = crearDevolucionUseCase.ejecutar(request);
            return new ResponseEntity<>(devolucion, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevolucionResponse> obtenerPorId(@PathVariable Long id) {
        return buscarDevolucionUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DevolucionResponse>> obtenerTodas(
            @RequestParam(required = false) Long ventaId) {

        if (ventaId != null) {
            return ResponseEntity.ok(buscarDevolucionUseCase.porVenta(ventaId));
        }
        return ResponseEntity.ok(buscarDevolucionUseCase.todas());
    }
}
