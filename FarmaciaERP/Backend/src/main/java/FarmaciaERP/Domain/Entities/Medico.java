package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EspecialidadMedica;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medico {
    private int id;
    private String nombre;
    private String cmp;
    private EspecialidadMedica especialidad;
    private String firma;

    public Medico() {}

    public Medico(int id, String nombre, String cmp, EspecialidadMedica especialidad, String firma) {
        this.id = id;
        this.nombre = nombre;
        this.cmp = cmp;
        this.especialidad = especialidad;
        this.firma = firma;
    }

}
