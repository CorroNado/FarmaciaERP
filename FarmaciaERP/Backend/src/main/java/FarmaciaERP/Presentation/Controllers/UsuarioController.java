package FarmaciaERP.presentation.controllers;

import FarmaciaERP.application.dto.Request.ActualizarUsuarioRequest;
import FarmaciaERP.application.dto.Request.CrearUsuarioRequest;
import FarmaciaERP.application.dto.Response.ActualizarUsuarioResponse;
import FarmaciaERP.application.dto.Response.CrearUsuarioResponse;
import FarmaciaERP.application.dto.Response.UserListResponse;
import FarmaciaERP.application.usecases.usuario.ActualizarUsuarioUseCase;
import FarmaciaERP.application.usecases.usuario.BuscarUsuarioUseCase;
import FarmaciaERP.application.usecases.usuario.CrearUsuarioUseCase;
import FarmaciaERP.application.usecases.usuario.EliminarUsuarioUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {


    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;

    public UsuarioController(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ActualizarUsuarioUseCase actualizarUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.actualizarUsuarioUseCase = actualizarUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CrearUsuarioRequest request) {
        try {
            CrearUsuarioResponse nuevoUsuario = crearUsuarioUseCase.ejecutar(request);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ActualizarUsuarioRequest request) {
        try {
            ActualizarUsuarioResponse response = actualizarUsuarioUseCase.ejecutar(id,request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            eliminarUsuarioUseCase.ejecutar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
        public ResponseEntity<UserListResponse> findById(@PathVariable Long id) {
        return buscarUsuarioUseCase.byId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserListResponse>> findALL() {

        return ResponseEntity.ok(buscarUsuarioUseCase.all());
    }
}
