package FarmaciaERP.Domain.ValueObjects;

import lombok.Value;

@Value
public class Dni {
    private String digitos;

    public Dni(String digitos){
        if (digitos == null || !digitos.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI inválido");
        }
        this.digitos = digitos;
    }
}
