package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * LOG.07 - Sucursal (punto de venta / farmacia capilar) destino de las
 * órdenes de traslado (STO) generadas en la Fase 06.
 */
@Getter
@Setter
public class Sucursal {

    private Long id;
    private String codigo;
    private String nombre;
    private boolean activa;

    private Sucursal() {
    }

    public Sucursal(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de la sucursal es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre de la sucursal es obligatorio");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.activa = true;
    }

    public static Sucursal reconstruir(Long id, String codigo, String nombre, boolean activa) {
        Sucursal sucursal = new Sucursal();
        sucursal.id = id;
        sucursal.codigo = codigo;
        sucursal.nombre = nombre;
        sucursal.activa = activa;
        return sucursal;
    }
}
