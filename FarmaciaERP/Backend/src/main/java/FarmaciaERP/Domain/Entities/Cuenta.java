package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Cuentas Â· nivel Cuenta (PCGE). Ej: "60 Compras", "70 Ventas".
 * Las SubcuentaDivisionaria cuelgan de una Cuenta.
 */
@Getter
@Setter
public class Cuenta {

    private Long id;
    private String codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;
    private NaturalezaCuenta naturaleza;
    private boolean activa;

    private Cuenta() {
    }

    public Cuenta(String codigo, String nombre, TipoCuenta tipoCuenta, NaturalezaCuenta naturaleza) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El cÃ³digo de la cuenta es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la cuenta es obligatorio");
        }
        if (tipoCuenta == null) {
            throw new BadRequestException("El tipo de cuenta es obligatorio");
        }
        if (naturaleza == null) {
            throw new BadRequestException("La naturaleza de la cuenta es obligatoria");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoCuenta = tipoCuenta;
        this.naturaleza = naturaleza;
        this.activa = true;
    }

    public static Cuenta reconstruir(Long id, String codigo, String nombre, TipoCuenta tipoCuenta,
                                      NaturalezaCuenta naturaleza, boolean activa) {
        Cuenta cuenta = new Cuenta();
        cuenta.id = id;
        cuenta.codigo = codigo;
        cuenta.nombre = nombre;
        cuenta.tipoCuenta = tipoCuenta;
        cuenta.naturaleza = naturaleza;
        cuenta.activa = activa;
        return cuenta;
    }

    public void desactivar() {
        this.activa = false;
    }
}