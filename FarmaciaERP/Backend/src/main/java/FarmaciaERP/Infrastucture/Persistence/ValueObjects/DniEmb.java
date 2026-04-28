package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;

@Embeddable
@Getter
public class DniEmb {
    private String digitos;

    protected DniEmb() {}

    public DniEmb(String digitos) {
        this.digitos = digitos;
    }
}
