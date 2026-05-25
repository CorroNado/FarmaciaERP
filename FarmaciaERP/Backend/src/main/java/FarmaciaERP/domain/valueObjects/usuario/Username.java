package FarmaciaERP.domain.valueObjects.usuario;

import lombok.Value;

@Value
public class Username {
    String valor;

    public Username(String valor) {
        this.valor = valor;
    }

    private void validarUsername(String valor){
        if(valor == null || valor.isBlank()) throw  new  RuntimeException("Ingrese un Nombre de User");
        if(!valor.matches(".{6,}")) throw  new  RuntimeException("El nombre de usuario debe tener minimo 6 caracteres");
    }
}
