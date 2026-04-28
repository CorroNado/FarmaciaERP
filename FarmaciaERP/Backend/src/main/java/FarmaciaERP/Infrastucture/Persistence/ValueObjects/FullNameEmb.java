package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import FarmaciaERP.Domain.ValueObjects.FullName;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class FullNameEmb {
    private String Nombres;
    private String Apellidos;

    public FullNameEmb(String nombres, String apellidos) {
        Nombres = nombres;
        Apellidos = apellidos;
    }

    protected FullNameEmb() {}
    public FullName toDomain() { return new FullName(Nombres, Apellidos); }
}
