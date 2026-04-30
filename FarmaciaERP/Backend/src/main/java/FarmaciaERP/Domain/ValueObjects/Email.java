package FarmaciaERP.Domain.ValueObjects;

import lombok.Value;

@Value
public class Email {
    private String email;

    public Email(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email requerido");
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        this.email = email;
    }
}