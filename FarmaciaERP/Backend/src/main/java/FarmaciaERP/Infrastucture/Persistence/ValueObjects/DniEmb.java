package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class DniEmb {

    @Column(name = "dni", nullable = false, unique = true, length = 10)
    private String dni;

    protected DniEmb() {}

    public DniEmb(String dni) {
        this.dni = dni;
    }
}
