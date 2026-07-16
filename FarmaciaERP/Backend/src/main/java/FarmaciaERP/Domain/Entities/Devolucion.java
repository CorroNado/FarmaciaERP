package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.AccionDevolucion;
import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Enums.MotivoDevolucion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SD.07 Devoluciones - Solicitud de devolución de uno o más medicamentos de
 * una venta ya pagada (VA01 · RE). Registra el motivo técnico (SD.07.01),
 * ejecuta la entrega inversa reingresando el stock devuelto (SD.07.02) y deja
 * constancia de la acción compensatoria acordada con el cliente (SD.07.03).
 * La devolución puede ser parcial: no siempre involucra todos los detalles
 * ni todas las cantidades de la venta de origen.
 */
@Getter
@Setter
public class Devolucion {
    private Long id;
    private Venta venta;
    private LocalDateTime fecha;
    private List<DetalleDevolucion> detalles = new ArrayList<>();
    private MotivoDevolucion motivo;
    private AccionDevolucion accion;

    public Devolucion() {
    }

    public Devolucion(Venta venta, List<DetalleDevolucion> detalles, MotivoDevolucion motivo, AccionDevolucion accion) {
        if (venta == null) {
            throw new BadRequestException("La devolución debe estar asociada a una venta");
        }
        if (venta.getEstado() != EstadoVenta.PAGADA) {
            throw new BadRequestException("Solo se pueden devolver ventas pagadas");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La devolución debe tener al menos un detalle");
        }
        if (motivo == null) {
            throw new BadRequestException("Debe indicar el motivo de la devolución");
        }
        if (accion == null) {
            throw new BadRequestException("Debe indicar la acción de la devolución");
        }
        for (DetalleDevolucion detalle : detalles) {
            DetalleVenta detalleVenta = venta.getDetalles().stream()
                    .filter(d -> d.getMedicamento().getId() == detalle.getMedicamento().getId())
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(
                            "El medicamento " + detalle.getMedicamento().getNombre() + " no pertenece a la venta indicada"));
            if (detalle.getCantidad() > detalleVenta.getCantidad()) {
                throw new BadRequestException(
                        "No se puede devolver una cantidad mayor a la vendida de " + detalle.getMedicamento().getNombre());
            }
        }

        this.venta = venta;
        this.detalles = new ArrayList<>(detalles);
        this.motivo = motivo;
        this.accion = accion;
        this.fecha = LocalDateTime.now();
    }

    public double getMonto() {
        return detalles.stream()
                .mapToDouble(DetalleDevolucion::getSubtotal)
                .sum();
    }

    /**
     * SD.07.02 - Entrega inversa: reingresa al stock las cantidades
     * efectivamente devueltas y cierra el ciclo de vida de la venta de origen.
     */
    public void aplicar() {
        for (DetalleDevolucion detalle : detalles) {
            Medicamento medicamento = detalle.getMedicamento();
            medicamento.setStock(medicamento.getStock() + detalle.getCantidad());
        }
        venta.devolver();
    }
}
