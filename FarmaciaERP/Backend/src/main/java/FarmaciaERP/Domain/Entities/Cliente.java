package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
    private Integer id;
    private FullName nombres;
    private Dni dni;
    private TipoSeguro tipoSeguro;

    public Cliente() {}

    public Cliente(int id, FullName nombres, Dni dni, TipoSeguro tipoSeguro) {
        this.id = id;
        this.nombres = nombres;
        this.dni = dni;
        this.tipoSeguro = tipoSeguro;
    }
}
