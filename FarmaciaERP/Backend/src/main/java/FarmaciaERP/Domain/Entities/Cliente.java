package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cliente {
    private Long id;
    private FullName nombres;
    private Dni dni;
    private TipoSeguro tipoSeguro;

    public Cliente() {}

    public Cliente(FullName nombres, Dni dni, TipoSeguro tipoSeguro) {
        this.nombres = nombres;
        this.dni = dni;
        this.tipoSeguro = tipoSeguro;
    }
}
