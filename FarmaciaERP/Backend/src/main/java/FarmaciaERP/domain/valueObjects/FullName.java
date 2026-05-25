package FarmaciaERP.domain.valueObjects;

import lombok.Value;

@Value
public class FullName{
    String nombres;
    String apellidos;

    public String getValue(){
        return nombres +" "+ apellidos;
    }
}
