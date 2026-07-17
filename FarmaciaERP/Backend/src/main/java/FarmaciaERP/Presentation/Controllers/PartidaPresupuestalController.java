package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearPartidaPresupuestalRequest;
import FarmaciaERP.Application.DTOs.Response.PartidaPresupuestalResponse;
import FarmaciaERP.Application.UseCases.BuscarPartidaPresupuestalUseCase;
import FarmaciaERP.Application.UseCases.CrearPartidaPresupuestalUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/partidas-presupuestales")
public class PartidaPresupuestalController {

    private final CrearPartidaPresupuestalUseCase crearPartidaUseCase;
    private final BuscarPartidaPresupuestalUseCase buscarPartidaUseCase;

    public PartidaPresupuestalController(CrearPartidaPresupuestalUseCase crearPartidaUseCase,
                                          BuscarPartidaPresupuestalUseCase buscarPartidaUseCase) {
        this.crearPartidaUseCase = crearPartidaUseCase;
        this.buscarPartidaUseCase = buscarPartidaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearPartidaPresupuestalRequest request) {
        try {
            PartidaPresupuestalResponse creada = crearPartidaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaPresupuestalResponse> obtenerPorId(@PathVariable Long id) {
        return buscarPartidaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PartidaPresupuestalResponse>> obtenerTodas(
            @RequestParam(required = false) Long centroCostoId) {
        return ResponseEntity.ok(centroCostoId != null
                ? buscarPartidaUseCase.porCentroCosto(centroCostoId)
                : buscarPartidaUseCase.todas());
    }
}