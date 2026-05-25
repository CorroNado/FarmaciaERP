package FarmaciaERP.domain.valueObjects;

import FarmaciaERP.domain.enums.TelephoneType;
import lombok.Value;

@Value
public class Telephone {
    String prefijo;
    String codigoArea;
    String numero;
    String descripcion;
    TelephoneType tipo;

    public Telephone(String prefijo, String numero, String descripcion) {
        this.prefijo = validarPrefijo(prefijo);
        this.codigoArea = null;
        this.numero = validarNumero(numero, TelephoneType.CELULAR);
        this.descripcion = descripcion;
        this.tipo = TelephoneType.CELULAR;
    }

    public Telephone(String prefijo, String codigoArea, String numero, String descripcion, TelephoneType tipo) {
        this.prefijo = validarPrefijo(prefijo);
        this.codigoArea = validarCodigoArea(codigoArea, tipo);
        this.numero = validarNumero(numero, tipo);
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    private String validarPrefijo(String prefijo) {
        if (prefijo == null || prefijo.isBlank())
            throw new IllegalArgumentException("Prefijo requerido");
        if (!prefijo.matches("\\+\\d{1,3}"))
            throw new IllegalArgumentException("Prefijo inválido, ejemplo: +51");
        return prefijo;
    }
    private String validarCodigoArea(String codigoArea, TelephoneType tipo) {
        if (tipo == TelephoneType.CELULAR) return null;
        if (codigoArea == null || codigoArea.isBlank())
            throw new IllegalArgumentException("Código de área requerido para teléfono fijo/fax");
        if (!codigoArea.matches("0\\d{1,2}"))
            throw new IllegalArgumentException("Código de área inválido, ejemplo: 01, 054");
        return codigoArea;
    }
    private String validarNumero(String numero, TelephoneType tipo) {
        if (numero == null || numero.isBlank())
            throw new IllegalArgumentException("Número requerido");

        String soloDigitos = numero.replaceAll("\\s", "");

        switch (tipo) {
            case CELULAR -> {
                if (!soloDigitos.matches("\\d{9}"))
                    throw new IllegalArgumentException("Celular debe tener 9 dígitos");
            }
            case FIJO -> {
                if (!soloDigitos.matches("\\d{7,8}"))
                    throw new IllegalArgumentException("Teléfono fijo debe tener 7 u 8 dígitos");
            }
            case FAX -> {
                if (!soloDigitos.matches("\\d{7,9}"))
                    throw new IllegalArgumentException("Fax debe tener entre 7 y 9 dígitos");
            }
        }
        return soloDigitos;
    }
}
