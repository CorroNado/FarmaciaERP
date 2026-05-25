package FarmaciaERP.domain.valueObjects;

import lombok.Value;

@Value
public class Dni {
    String valor;

    public Dni(String valor){
        if (valor == null || !valor.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI inválido");
        }
        this.valor = valor;
    }
}
