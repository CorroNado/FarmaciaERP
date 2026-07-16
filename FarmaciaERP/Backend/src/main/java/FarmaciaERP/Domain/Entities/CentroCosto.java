package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * Plan de Costos Â· Centro de Costo. Agrupa el gasto/costo por Ã¡rea o
 * sucursal. La sucursal es opcional (hay centros de costo transversales,
 * ej. "AdministraciÃ³n Central").
 */
@Getter
@Setter
public class CentroCosto {

    private Long id;
    private String codigo;
    private String nombre;
    private Sucursal sucursal;
    private boolean activo;

    private CentroCosto() {
    }

    public CentroCosto(String codigo, String nombre, Sucursal sucursal) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El cÃ³digo del centro de costo es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre del centro de costo es obligatorio");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.sucursal = sucursal;
        this.activo = true;
    }

    public static CentroCosto reconstruir(Long id, String codigo, String nombre, Sucursal sucursal,
                                           boolean activo) {
        CentroCosto centroCosto = new CentroCosto();
        centroCosto.id = id;
        centroCosto.codigo = codigo;
        centroCosto.nombre = nombre;
        centroCosto.sucursal = sucursal;
        centroCosto.activo = activo;
        return centroCosto;
    }

    public void desactivar() {
        this.activo = false;
    }
}