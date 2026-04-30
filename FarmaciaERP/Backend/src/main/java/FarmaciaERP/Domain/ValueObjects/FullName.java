package FarmaciaERP.Domain.ValueObjects;

import lombok.Value;

@Value
public class FullName{
    private String Nombres;
    private String Apellidos;

    public String getValue(){
        return Nombres+" "+Apellidos;
    }
}
