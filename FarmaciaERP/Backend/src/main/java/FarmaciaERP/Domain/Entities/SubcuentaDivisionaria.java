package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Cuentas Â· nivel Subcuenta Divisionaria (PCGE). Ej: "601
 * MercaderÃ­as" cuelga de la Cuenta "60 Compras". RN: el cÃ³digo debe
 * iniciar con el cÃ³digo de la cuenta padre (jerarquÃ­a PCGE).
 */
@Getter
@Setter
public class SubcuentaDivisionaria {

    private Long id;
    private String codigo;
    private String nombre;
    private Cuenta cuenta;
    private boolean activa;

    private SubcuentaDivisionaria() {
    }

    public SubcuentaDivisionaria(String codigo, String nombre, Cuenta cuenta) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El cÃ³digo de la subcuenta divisionaria es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la subcuenta divisionaria es obligatorio");
        }
        if (cuenta == null) {
            throw new BadRequestException("La subcuenta divisionaria debe pertenecer a una cuenta");
        }
        if (!codigo.startsWith(cuenta.getCodigo())) {
            throw new BadRequestException(
                    "El cÃ³digo " + codigo + " debe iniciar con el cÃ³digo de la cuenta padre (" + cuenta.getCodigo() + ")");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuenta = cuenta;
        this.activa = true;
    }

    public static SubcuentaDivisionaria reconstruir(Long id, String codigo, String nombre, Cuenta cuenta,
                                                      boolean activa) {
        SubcuentaDivisionaria subcuenta = new SubcuentaDivisionaria();
        subcuenta.id = id;
        subcuenta.codigo = codigo;
        subcuenta.nombre = nombre;
        subcuenta.cuenta = cuenta;
        subcuenta.activa = activa;
        return subcuenta;
    }

    public void desactivar() {
        this.activa = false;
    }
}