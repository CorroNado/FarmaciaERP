package FarmaciaERP.domain.valueObjects;

import lombok.Value;

@Value
public class EmailAddress {
    String direccion;

    public EmailAddress(String direccion) {
        this.direccion = verificateAddress(direccion);
    }

    private String verificateAddress(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EmailContact requerido");
        }
        if (!value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de direccion inválido");
        }return value;
    }
}
