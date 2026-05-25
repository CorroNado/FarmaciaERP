package FarmaciaERP.domain.valueObjects.usuario;

import lombok.Value;

@Value
public class Password {
    String valor;

    public Password(String valor) {
        ValidarPassword(valor);
        this.valor = valor;
    }
    private void ValidarPassword(String valor) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("La contraseña no puede estar vacia");
        if (!valor.matches(".{8,}")) throw  new IllegalArgumentException("La contraseña debe tener minimo 8 caracteres");
        if (!valor.matches(".*[a-záéíóúñ].*")) throw  new IllegalArgumentException("La contraseña debe contener al menos una letra minuscula");
        if (!valor.matches(".*[A-ZÁÉÍÓÚÑ].*")) throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        if (!valor.matches("[0-9]")) throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        if (!valor.matches(".*[^a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ].*")) throw new IllegalArgumentException("La contraseña debe contener al menos un carácter especial (ej: !, @, #, $)");
    }
}
