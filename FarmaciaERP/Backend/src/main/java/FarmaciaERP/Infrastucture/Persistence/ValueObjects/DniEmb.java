package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DniEmb {
    private String dni;

    protected DniEmb() {}

    public DniEmb(String dni) {
        this.dni = dni;
    }
}
