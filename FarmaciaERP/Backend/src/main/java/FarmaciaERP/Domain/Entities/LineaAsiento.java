package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Detalle o partida individual dentro de un Asiento Contable.
 * Vincula una subcuenta divisionaria con un monto al Debe o al Haber.
 */
@Getter
@Setter
public class LineaAsiento {

    private Long id;
    private SubcuentaDivisionaria subcuenta;
    private CentroCosto centroCosto;
    private BigDecimal debe;
    private BigDecimal haber;
    private String glosaDetalle;

    private LineaAsiento() {
    }

    public LineaAsiento(SubcuentaDivisionaria subcuenta, CentroCosto centroCosto,
                        BigDecimal debe, BigDecimal haber, String glosaDetalle) {
        if (subcuenta == null) {
            throw new BadRequestException("La lÃ­nea del asiento debe tener una subcuenta asociada");
        }
        if (debe == null) debe = BigDecimal.ZERO;
        if (haber == null) haber = BigDecimal.ZERO;

        if (debe.compareTo(BigDecimal.ZERO) < 0 || haber.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Los montos al Debe o Haber no pueden ser negativos");
        }
        if (debe.compareTo(BigDecimal.ZERO) > 0 && haber.compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Una misma lÃ­nea no puede tener montos en el Debe y en el Haber simultÃ¡neamente");
        }
        if (debe.compareTo(BigDecimal.ZERO) == 0 && haber.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("La lÃ­nea debe tener un monto mayor a cero en el Debe o en el Haber");
        }

        this.subcuenta = subcuenta;
        this.centroCosto = centroCosto;
        this.debe = debe;
        this.haber = haber;
        this.glosaDetalle = glosaDetalle;
    }

    public static LineaAsiento reconstruir(Long id, SubcuentaDivisionaria subcuenta, CentroCosto centroCosto,
                                           BigDecimal debe, BigDecimal haber, String glosaDetalle) {
        LineaAsiento linea = new LineaAsiento();
        linea.id = id;
        linea.subcuenta = subcuenta;
        linea.centroCosto = centroCosto;
        linea.debe = debe;
        linea.haber = haber;
        linea.glosaDetalle = glosaDetalle;
        return linea;
    }
}