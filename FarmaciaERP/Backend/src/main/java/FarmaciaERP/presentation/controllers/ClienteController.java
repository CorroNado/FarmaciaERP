//package FarmaciaERP.presentation.controllers;
//
//import FarmaciaERP.application.dto.Request.CrearClienteRequest;
//import FarmaciaERP.application.dto.Response.CrearClienteResponse;
//import FarmaciaERP.application.usecases.cliente.ActualizarClienteUseCase;
//import FarmaciaERP.application.usecases.cliente.BuscarClienteUseCase;
//import FarmaciaERP.application.usecases.cliente.CrearClienteUseCase;
//import FarmaciaERP.application.usecases.cliente.EliminarClienteUseCase;
//import FarmaciaERP.domain.entities.Customer;
//import FarmaciaERP.domain.enums.InsuranceType;
//import FarmaciaERP.domain.valueObjects.Dni;
//import FarmaciaERP.domain.valueObjects.FullName;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/cliente")
//public class ClienteController {
//
//    private final CrearClienteUseCase crearClienteUseCase;
//    private final ActualizarClienteUseCase actualizarClienteUseCase;
//    private final EliminarClienteUseCase eliminarClienteUseCase;
//    private final BuscarClienteUseCase buscarClienteUseCase;
//
//    public ClienteController(
//            CrearClienteUseCase crearClienteUseCase,
//            ActualizarClienteUseCase actualizarClienteUseCase,
//            EliminarClienteUseCase eliminarClienteUseCase,
//            BuscarClienteUseCase buscarClienteUseCase) {
//        this.crearClienteUseCase = crearClienteUseCase;
//        this.actualizarClienteUseCase = actualizarClienteUseCase;
//        this.eliminarClienteUseCase = eliminarClienteUseCase;
//        this.buscarClienteUseCase = buscarClienteUseCase;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> crear(@RequestBody CrearClienteRequest request) {
//        try {
//            System.out.println("POST CLIENTE EJECUTANDO");
//            CrearClienteResponse nuevoPaciente = crearClienteUseCase.ejecutar(request);
//            return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @PutMapping("/{userId}")
//    public ResponseEntity<Customer> actualizar(@PathVariable Long distritoId, @RequestBody Customer customer) {
//        try {
//            Customer customerActualizado = actualizarClienteUseCase.ejecutar(distritoId, customer);
//            return ResponseEntity.ok(customerActualizado);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> eliminar(@PathVariable Long distritoId) {
//        try {
//            eliminarClienteUseCase.ejecutar(distritoId);
//            return ResponseEntity.noContent().build(); // 204 No Content
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<Customer> obtenerPorId(@PathVariable Long distritoId) {
//        return buscarClienteUseCase.porId(distritoId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Customer>> obtenerTodos(
//            @RequestParam(required = false) FullName nombres,
//            @RequestParam(required = false) Dni dni,
//            @RequestParam(required = false) InsuranceType insuranceType) {
//        if (dni != null) {
//            return buscarClienteUseCase.porDocumento(dni)
//                    .map(paciente -> ResponseEntity.ok(List.of(paciente)))
//                    .orElse(ResponseEntity.notFound().build());
//        }
//
//        if (nombres != null) {
//            return ResponseEntity.ok(buscarClienteUseCase.porNombre(nombres));
//        }
//
//        if (insuranceType != null) {
//            return ResponseEntity.ok(buscarClienteUseCase.porTipoSeguro(insuranceType));
//        }
//
//        return ResponseEntity.ok(buscarClienteUseCase.todos());
//    }
//}
