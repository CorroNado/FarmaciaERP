package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.UseCases.BuscarMedicamentoUseCase;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Enums.MedicamentoCategoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamento")
public class MedicamentoController {

    private final BuscarMedicamentoUseCase buscarMedicamentoUseCase;

    public MedicamentoController(BuscarMedicamentoUseCase buscarMedicamentoUseCase) {
        this.buscarMedicamentoUseCase = buscarMedicamentoUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> obtenerPorId(@PathVariable int id) {
        return buscarMedicamentoUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Medicamento>> obtenerTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) MedicamentoCategoria categoria) {

        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(buscarMedicamentoUseCase.porNombre(nombre));
        }
        if (categoria != null) {
            return ResponseEntity.ok(buscarMedicamentoUseCase.porCategoria(categoria));
        }
        return ResponseEntity.ok(buscarMedicamentoUseCase.todos());
    }
}
