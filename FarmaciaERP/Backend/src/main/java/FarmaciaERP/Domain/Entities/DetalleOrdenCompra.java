package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * LOG.03 - Línea de la Orden de Compra. El precio se hereda bloqueado del
 * Info-Record del convenio (RN-OC-002) y no es editable por el comprador.
 */
@Getter
@Setter
public class DetalleOrdenCompra {
    private Long id;
    private Medicamento medicamento;
    private int cantidad;
    private double precioUnitario;

    public DetalleOrdenCompra() {
    }

    public DetalleOrdenCompra(Medicamento medicamento, int cantidad, double precioUnitario) {
        if (medicamento == null) {
            throw new BadRequestException("El detalle de la Orden de Compra debe referenciar un medicamento");
        }
        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad de la Orden de Compra debe ser mayor a 0");
        }
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}
