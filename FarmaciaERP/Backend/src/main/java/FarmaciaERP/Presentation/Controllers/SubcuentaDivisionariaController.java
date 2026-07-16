package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearSubcuentaDivisionariaRequest;
import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Application.UseCases.BuscarSubcuentaDivisionariaUseCase;
import FarmaciaERP.Application.UseCases.CrearSubcuentaDivisionariaUseCase;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contabilidad/subcuentas")
public class SubcuentaDivisionariaController {

    private final CrearSubcuentaDivisionariaUseCase crearSubcuentaUseCase;
    private final BuscarSubcuentaDivisionariaUseCase buscarSubcuentaUseCase;

    public SubcuentaDivisionariaController(CrearSubcuentaDivisionariaUseCase crearSubcuentaUseCase,
                                            BuscarSubcuentaDivisionariaUseCase buscarSubcuentaUseCase) {
        this.crearSubcuentaUseCase = crearSubcuentaUseCase;
        this.buscarSubcuentaUseCase = buscarSubcuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearSubcuentaDivisionariaRequest request) {
        try {
            SubcuentaDivisionariaResponse creada = crearSubcuentaUseCase.ejecutar(request);
            return new ResponseEntity<>(creada, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcuentaDivisionariaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarSubcuentaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SubcuentaDivisionariaResponse>> obtenerTodas(
            @RequestParam(required = false) Long cuentaId) {
        return ResponseEntity.ok(cuentaId != null
                ? buscarSubcuentaUseCase.porCuenta(cuentaId)
                : buscarSubcuentaUseCase.todas());
    }
}