package FarmaciaERP.Domain.Entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleReceta {
    private Medicamento medicamento;
    private int cantidad;

    public DetalleReceta(Medicamento medicamento, int cantidad) {
        if (medicamento == null) {
            throw new IllegalArgumentException("Medicamento no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad debe ser mayor a 0");
        }
        this.medicamento = medicamento;
        this.cantidad = cantidad;
    }
}
