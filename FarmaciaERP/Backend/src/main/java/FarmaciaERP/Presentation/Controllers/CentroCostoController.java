package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearCentroCostoRequest;
import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Application.UseCases.BuscarCentroCostoUseCase;
import FarmaciaERP.Application.UseCases.CrearCentroCostoUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/centros-costo")
public class CentroCostoController {

    private final CrearCentroCostoUseCase crearCentroCostoUseCase;
    private final BuscarCentroCostoUseCase buscarCentroCostoUseCase;

    public CentroCostoController(CrearCentroCostoUseCase crearCentroCostoUseCase,
                                  BuscarCentroCostoUseCase buscarCentroCostoUseCase) {
        this.crearCentroCostoUseCase = crearCentroCostoUseCase;
        this.buscarCentroCostoUseCase = buscarCentroCostoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearCentroCostoRequest request) {
        try {
            CentroCostoResponse creado = crearCentroCostoUseCase.ejecutar(request);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroCostoResponse> obtenerPorId(@PathVariable Long id) {
        return buscarCentroCostoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CentroCostoResponse>> obtenerTodos(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        return ResponseEntity.ok(soloActivos ? buscarCentroCostoUseCase.activos() : buscarCentroCostoUseCase.todos());
    }
}