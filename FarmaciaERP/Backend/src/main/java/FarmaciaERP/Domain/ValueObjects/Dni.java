package FarmaciaERP.Domain.ValueObjects;

import lombok.Value;

@Value
public class Dni {
    private String dni;

    public Dni(String dni){
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI inválido");
        }
        this.dni = dni;
    }
}
