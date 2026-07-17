package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Partida presupuestal asociada a un centro de costo, usada para comparar
 * presupuesto vs. ejecuciÃ³n real.
 */
@Getter
@Setter
public class PartidaPresupuestal {

    private Long id;
    private String codigo;
    private String nombre;
    private CentroCosto centroCosto;
    private BigDecimal montoPresupuestado;
    private boolean activa;

    private PartidaPresupuestal() {
    }

    public PartidaPresupuestal(String codigo, String nombre, CentroCosto centroCosto,
                                BigDecimal montoPresupuestado) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El cÃ³digo de la partida presupuestal es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la partida presupuestal es obligatorio");
        }
        if (centroCosto == null) {
            throw new BadRequestException("La partida presupuestal debe estar asociada a un centro de costo");
        }
        if (montoPresupuestado == null || montoPresupuestado.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("El monto presupuestado no puede ser negativo");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.centroCosto = centroCosto;
        this.montoPresupuestado = montoPresupuestado;
        this.activa = true;
    }

    public static PartidaPresupuestal reconstruir(Long id, String codigo, String nombre, CentroCosto centroCosto,
                                                    BigDecimal montoPresupuestado, boolean activa) {
        PartidaPresupuestal partida = new PartidaPresupuestal();
        partida.id = id;
        partida.codigo = codigo;
        partida.nombre = nombre;
        partida.centroCosto = centroCosto;
        partida.montoPresupuestado = montoPresupuestado;
        partida.activa = activa;
        return partida;
    }

    public void desactivar() {
        this.activa = false;
    }
}