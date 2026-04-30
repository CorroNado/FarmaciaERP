package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import FarmaciaERP.Domain.ValueObjects.FullName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class FullNameEmb {

    @Column(name = "nombres", nullable = true, length = 100)
    private String Nombres;

    @Column(name = "apellidos", nullable = true, length = 100)
    private String Apellidos;

    public FullNameEmb(String nombres, String apellidos) {
        Nombres = nombres;
        Apellidos = apellidos;
    }

    protected FullNameEmb() {}

    public String getValue() {
        return Nombres + " " + Apellidos;
    }
}
