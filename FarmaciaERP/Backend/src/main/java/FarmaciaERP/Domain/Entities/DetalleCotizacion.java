package FarmaciaERP.Domain.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleCotizacion {
    private Long id;
    private Medicamento medicamento;
    private int cantidad;
    private double precioUnitario;

    public DetalleCotizacion() {
    }

    public DetalleCotizacion(Medicamento medicamento, int cantidad, double precioUnitario) {
        if (medicamento == null) {
            throw new IllegalArgumentException("Medicamento no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("Precio unitario no puede ser negativo");
        }
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}
