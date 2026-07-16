package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * LOG.02 - Info-Record (ME11): congela el precio pactado de un medicamento
 * dentro de un Convenio Marco vigente (RN-MM-004).
 */
@Getter
@Setter
public class ItemConvenio {
    private Long id;
    private Medicamento medicamento;
    private double precioPactado;

    public ItemConvenio() {
    }

    public ItemConvenio(Medicamento medicamento, double precioPactado) {
        if (medicamento == null) {
            throw new BadRequestException("El ítem de convenio debe referenciar un medicamento");
        }
        if (precioPactado <= 0) {
            throw new BadRequestException("El precio pactado debe ser mayor a 0");
        }
        this.medicamento = medicamento;
        this.precioPactado = precioPactado;
    }
}
