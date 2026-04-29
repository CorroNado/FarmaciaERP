package FarmaciaERP.Domain.ValueObjects;

import lombok.Value;

@Value
public class Email {
    private String email;

    public Email(String email) {
        if (email == null ||
                !email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            throw new IllegalArgumentException("Correo Gmail inválido");
        }

        this.email = email;
    }
}