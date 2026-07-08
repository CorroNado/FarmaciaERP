package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Enums.MetodoPago;
import FarmaciaERP.Domain.Enums.TipoComprobante;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Exceptions.StockInsuficienteException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Venta {
    private Long id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private List<DetalleVenta> detalles = new ArrayList<>();
    private EstadoVenta estado;
    private MetodoPago metodoPago;
    private TipoComprobante tipoComprobante;
    private String numeroComprobante;

    public Venta() {
    }

    public Venta(Cliente cliente, List<DetalleVenta> detalles, MetodoPago metodoPago, TipoComprobante tipoComprobante) {
        if (cliente == null) {
            throw new BadRequestException("La venta debe estar asociada a un cliente");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La venta debe tener al menos un detalle");
        }
        this.cliente = cliente;
        this.detalles = new ArrayList<>(detalles);
        this.metodoPago = metodoPago;
        this.tipoComprobante = tipoComprobante != null ? tipoComprobante : TipoComprobante.NINGUNO;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoVenta.PENDIENTE;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (detalle == null) {
            throw new BadRequestException("Detalle no puede ser null");
        }
        detalles.add(detalle);
    }

    public void quitarDetalle(DetalleVenta detalle) {
        if (detalle == null) {
            throw new BadRequestException("Detalle no puede ser null");
        }
        detalles.remove(detalle);
    }

    public double getTotal() {
        return detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
    }

    /**
     * SD.03.02 / SD.03.03 - Verifica disponibilidad de stock y ejecuta el descuento
     * de cada medicamento involucrado en la venta.
     */
    public void confirmarStock() {
        for (DetalleVenta detalle : detalles) {
            Medicamento medicamento = detalle.getMedicamento();
            if (!medicamento.estaDisponible() || medicamento.getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para el medicamento: " + medicamento.getNombre());
            }
            if (medicamento.requiereReceta()) {
                throw new BadRequestException(
                        "El medicamento " + medicamento.getNombre() + " requiere receta médica");
            }
        }
        for (DetalleVenta detalle : detalles) {
            Medicamento medicamento = detalle.getMedicamento();
            medicamento.setStock(medicamento.getStock() - detalle.getCantidad());
        }
    }

    public void registrarPago() {
        cambiarEstado(EstadoVenta.PAGADA);
    }

    /**
     * SD.07 Devoluciones - repone el stock de cada detalle al anular/devolver.
     */
    public void reponerStock() {
        for (DetalleVenta detalle : detalles) {
            Medicamento medicamento = detalle.getMedicamento();
            medicamento.setStock(medicamento.getStock() + detalle.getCantidad());
        }
    }

    public void anular() {
        cambiarEstado(EstadoVenta.ANULADA);
        reponerStock();
    }

    public void devolver() {
        cambiarEstado(EstadoVenta.DEVUELTA);
        reponerStock();
    }

    private void cambiarEstado(EstadoVenta nuevoEstado) {
        if (!estado.puedeCambiarA(nuevoEstado)) {
            throw new BadRequestException(
                    "No se puede cambiar el estado de " + estado + " a " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }
}
