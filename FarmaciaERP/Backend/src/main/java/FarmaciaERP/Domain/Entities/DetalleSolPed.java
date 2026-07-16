package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * LOG.01 - Línea de necesidad de abastecimiento calculada por el MRP
 * (RN-E1-001, RN-E1-002).
 */
@Getter
@Setter
public class DetalleSolPed {
    private Long id;
    private Medicamento medicamento;
    private int stockActual;
    private int stockMinimo;
    private int cantidadSugerida;
    private double precioUnitario;

    public DetalleSolPed() {
    }

    public DetalleSolPed(Medicamento medicamento, int stockActual, int stockMinimo,
                          int cantidadSugerida, double precioUnitario) {
        if (medicamento == null) {
            throw new BadRequestException("El detalle de la SolPed debe referenciar un medicamento");
        }
        // RN-E1-006: no se permiten cantidades nulas o iguales a cero.
        if (cantidadSugerida <= 0) {
            throw new BadRequestException(
                    "RN-E1-006: la cantidad a solicitar de " + medicamento.getNombre() + " debe ser mayor a 0");
        }
        if (precioUnitario < 0) {
            throw new BadRequestException("El precio unitario no puede ser negativo");
        }
        this.medicamento = medicamento;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.cantidadSugerida = cantidadSugerida;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidadSugerida * precioUnitario;
    }

    /**
     * RN-E1-002: la necesidad de abastecimiento surge cuando el stock actual
     * está por debajo del stock mínimo de seguridad.
     */
    public boolean estaPorDebajoDelMinimo() {
        return stockActual < stockMinimo;
    }
}
