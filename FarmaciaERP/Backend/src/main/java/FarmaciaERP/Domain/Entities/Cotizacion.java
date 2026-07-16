package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SD.02 Cotizaciones - Construcción y seguimiento de ofertas económicas
 * previas a la generación del pedido de venta (SD.03).
 */
@Getter
@Setter
public class Cotizacion {
    private Long id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private int vigenciaDias;
    private List<DetalleCotizacion> detalles = new ArrayList<>();
    private EstadoCotizacion estado;
    private String motivoRechazo;
    private Long ventaGeneradaId;

    public Cotizacion() {
    }

    public Cotizacion(Cliente cliente, List<DetalleCotizacion> detalles, int vigenciaDias) {
        if (cliente == null) {
            throw new BadRequestException("La cotización debe estar asociada a un cliente");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La cotización debe tener al menos un detalle");
        }
        if (vigenciaDias <= 0) {
            throw new BadRequestException("La vigencia de la cotización debe ser mayor a 0 días");
        }
        this.cliente = cliente;
        this.detalles = new ArrayList<>(detalles);
        this.vigenciaDias = vigenciaDias;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoCotizacion.PENDIENTE;
    }

    public double getTotal() {
        return detalles.stream()
                .mapToDouble(DetalleCotizacion::getSubtotal)
                .sum();
    }

    public LocalDateTime getFechaVencimiento() {
        return fecha != null ? fecha.plusDays(vigenciaDias) : null;
    }

    /**
     * SD.02.03 - Una cotización deja de ser válida para su conversión una vez
     * superada su vigencia, aunque siga marcada como PENDIENTE.
     */
    public boolean estaVigente() {
        return getFechaVencimiento() != null && !LocalDateTime.now().isAfter(getFechaVencimiento());
    }

    /**
     * SD.02.03 - El cliente acepta la oferta: la cotización queda lista para
     * convertirse en pedido de venta (SD.03.01).
     */
    public void aceptar(Long ventaGeneradaId) {
        if (!estaVigente()) {
            throw new BadRequestException("La cotización ya no está vigente y no puede aceptarse");
        }
        cambiarEstado(EstadoCotizacion.ACEPTADA);
        this.ventaGeneradaId = ventaGeneradaId;
    }

    /**
     * SD.02.04 - El cliente rechaza la oferta, registrando el motivo.
     */
    public void rechazar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException("Debe indicar el motivo de rechazo de la cotización");
        }
        cambiarEstado(EstadoCotizacion.RECHAZADA);
        this.motivoRechazo = motivo;
    }

    public void vencer() {
        cambiarEstado(EstadoCotizacion.VENCIDA);
    }

    private void cambiarEstado(EstadoCotizacion nuevoEstado) {
        if (!estado.puedeCambiarA(nuevoEstado)) {
            throw new BadRequestException(
                    "No se puede cambiar el estado de " + estado + " a " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }
}
