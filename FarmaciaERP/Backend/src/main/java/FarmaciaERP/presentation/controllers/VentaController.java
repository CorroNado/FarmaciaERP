package FarmaciaERP.presentation.controllers;

import FarmaciaERP.application.dto.Request.CrearVentaRequest;
import FarmaciaERP.application.dto.Response.VentaResponse;
import FarmaciaERP.application.usecases.AnularVentaUseCase;
import FarmaciaERP.application.usecases.BuscarVentaUseCase;
import FarmaciaERP.application.usecases.CrearVentaUseCase;
import FarmaciaERP.application.usecases.RegistrarPagoVentaUseCase;
import FarmaciaERP.domain.enums.EstadoVenta;
import FarmaciaERP.domain.exceptions.BadRequestException;
import FarmaciaERP.domain.exceptions.StockInsuficienteException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    private final CrearVentaUseCase crearVentaUseCase;
    private final AnularVentaUseCase anularVentaUseCase;
    private final RegistrarPagoVentaUseCase registrarPagoVentaUseCase;
    private final BuscarVentaUseCase buscarVentaUseCase;

    public VentaController(CrearVentaUseCase crearVentaUseCase,
                            AnularVentaUseCase anularVentaUseCase,
                            RegistrarPagoVentaUseCase registrarPagoVentaUseCase,
                            BuscarVentaUseCase buscarVentaUseCase) {
        this.crearVentaUseCase = crearVentaUseCase;
        this.anularVentaUseCase = anularVentaUseCase;
        this.registrarPagoVentaUseCase = registrarPagoVentaUseCase;
        this.buscarVentaUseCase = buscarVentaUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearVentaRequest request) {
        try {
            VentaResponse venta = crearVentaUseCase.ejecutar(request);
            return new ResponseEntity<>(venta, HttpStatus.CREATED);
        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<?> registrarPago(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(registrarPagoVentaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(anularVentaUseCase.ejecutar(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(@PathVariable Long id) {
        return buscarVentaUseCase.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> obtenerTodas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        if (clienteId != null) {
            return ResponseEntity.ok(buscarVentaUseCase.porCliente(clienteId));
        }
        if (estado != null) {
            return ResponseEntity.ok(buscarVentaUseCase.porEstado(estado));
        }
        if (fecha != null) {
            return ResponseEntity.ok(buscarVentaUseCase.porFecha(fecha));
        }
        return ResponseEntity.ok(buscarVentaUseCase.todas());
    }
}
