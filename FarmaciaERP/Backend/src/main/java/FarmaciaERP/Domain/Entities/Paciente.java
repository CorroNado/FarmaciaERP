package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.TipoSeguro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paciente {
    private int id;
    private String nombre;
    private String dni;
    private TipoSeguro tipoSeguro;

    public Paciente() {}

    public Paciente(int id, String nombre, String dni, TipoSeguro tipoSeguro) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.tipoSeguro = tipoSeguro;
    }
}
