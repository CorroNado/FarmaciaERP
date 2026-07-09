package FarmaciaERP.domain.valueObjects.usuario;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class Password {
    String valor;

    private Password(String valor, boolean bypassValidation) {
        this.valor = valor;
    }
    public Password(String valor) {
        this.valor = ValidarPassword(valor);
    }
    private String ValidarPassword(String valor) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("La contraseña no puede estar vacia");
        if (!valor.matches(".{8,}"))
            throw new IllegalArgumentException("La contraseña debe tener minimo 8 caracteres");
        if (!valor.matches(".*[a-záéíóúñü].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minuscula");
        if (!valor.matches(".*[A-ZÁÉÍÓÚÑÜ].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        if (!valor.matches(".*[0-9].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        if (!valor.matches(".*[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial (ej: !, @, #, $)");

        return valor;
    }

    public static Password setHashPassword(String hashDesdeBD) {
        return new Password(hashDesdeBD, true);
    }
}
