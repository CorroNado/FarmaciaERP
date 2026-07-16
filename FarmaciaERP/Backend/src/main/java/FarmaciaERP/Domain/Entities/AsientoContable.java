package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera del Asiento Contable (Libro Diario).
 * Contiene la lÃ³gica para validar que la transacciÃ³n estÃ© cuadrada (Partida Doble).
 */
@Getter
@Setter
public class AsientoContable {

    private Long id;
    private String numero;
    private LocalDate fechaContable;
    private String glosa;
    private TipoAsiento tipoAsiento;
    private EstadoAsiento estado;
    private List<LineaAsiento> lineas = new ArrayList<>();

    private AsientoContable() {
    }

    public AsientoContable(String numero, LocalDate fechaContable, String glosa,
                           TipoAsiento tipoAsiento, List<LineaAsiento> lineas) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El nÃºmero de asiento es obligatorio");
        }
        if (fechaContable == null) {
            throw new BadRequestException("La fecha contable es obligatoria");
        }
        if (glosa == null || glosa.isBlank()) {
            throw new BadRequestException("La glosa o explicaciÃ³n del asiento es obligatoria");
        }
        if (tipoAsiento == null) {
            throw new BadRequestException("El tipo de asiento es obligatorio");
        }
        if (lineas == null || lineas.size() < 2) {
            throw new BadRequestException("Un asiento contable debe tener al menos dos lÃ­neas para cumplir la partida doble");
        }

        this.numero = numero;
        this.fechaContable = fechaContable;
        this.glosa = glosa;
        this.tipoAsiento = tipoAsiento;
        this.estado = EstadoAsiento.BORRADOR;
        this.lineas = lineas;

        validarPartidaDoble();
    }

    public static AsientoContable reconstruir(Long id, String numero, LocalDate fechaContable, String glosa,
                                              TipoAsiento tipoAsiento, EstadoAsiento estado, List<LineaAsiento> lineas) {
        AsientoContable asiento = new AsientoContable();
        asiento.id = id;
        asiento.numero = numero;
        asiento.fechaContable = fechaContable;
        asiento.glosa = glosa;
        asiento.tipoAsiento = tipoAsiento;
        asiento.estado = estado;
        asiento.lineas = lineas;
        return asiento;
    }

    /**
     * Valida la ecuaciÃ³n contable fundamental: Sumatoria del Debe == Sumatoria del Haber.
     */
    public void validarPartidaDoble() {
        BigDecimal totalDebe = calcularTotalDebe();
        BigDecimal totalHaber = calcularTotalHaber();

        if (totalDebe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto total del asiento debe ser mayor a cero");
        }
        if (totalDebe.compareTo(totalHaber) != 0) {
            throw new BadRequestException(String.format(
                "Asiento descuadrado (Partida Doble): Total Debe (S/ %s) no coincide con Total Haber (S/ %s)",
                totalDebe, totalHaber));
        }
    }

    public BigDecimal calcularTotalDebe() {
        return lineas.stream()
                .map(LineaAsiento::getDebe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalHaber() {
        return lineas.stream()
                .map(LineaAsiento::getHaber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void contabilizar() {
        if (this.estado != EstadoAsiento.BORRADOR) {
            throw new BadRequestException("Solo se pueden contabilizar asientos en estado BORRADOR");
        }
        validarPartidaDoble();
        this.estado = EstadoAsiento.CONTABILIZADO;
    }

    public void anular() {
        if (this.estado == EstadoAsiento.ANULADO) {
            throw new BadRequestException("El asiento ya se encuentra anulado");
        }
        this.estado = EstadoAsiento.ANULADO;
    }
}