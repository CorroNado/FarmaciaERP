package FarmaciaERP.Presentation.Controllers;

import FarmaciaERP.Application.DTOs.Request.CrearUsuarioResquest;
import FarmaciaERP.Application.DTOs.Response.CrearUsuarioResponse;
import FarmaciaERP.Application.UseCases.ActualizarUsuarioUseCase;
import FarmaciaERP.Application.UseCases.BuscarUsuarioUseCase;
import FarmaciaERP.Application.UseCases.CrearUsuarioUseCase;
import FarmaciaERP.Application.UseCases.EliminarUsuarioUseCase;
import FarmaciaERP.Domain.Entities.Usuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<?> crear(@RequestBody CrearUsuarioResquest request) {
        try {
            CrearUsuarioResponse nuevoUsuario = crearUsuarioUseCase.ejecutar(request);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = actualizarUsuarioUseCase.ejecutar(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        try {
            eliminarUsuarioUseCase.ejecutar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
        public ResponseEntity<Usuario> obtenerPorId(@PathVariable Email gmail) {
        return buscarUsuarioUseCase.porEmail(gmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos(
            @RequestParam(required = false) FullName nombre,
            @RequestParam(required = false) Email email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) UsuarioEstados estados) {

        if (email != null) {
            return buscarUsuarioUseCase.porEmail(email)
                    .map(usuario -> ResponseEntity.ok(List.of(usuario)))
                    .orElse(ResponseEntity.notFound().build());
        }

        if (nombre != null) {
            return ResponseEntity.ok(buscarUsuarioUseCase.porNombre(nombre));
        }

        if (estados != null) {
            return ResponseEntity.ok(buscarUsuarioUseCase.porEstado(estados));
        }

        return ResponseEntity.ok(buscarUsuarioUseCase.todos());
    }
}
