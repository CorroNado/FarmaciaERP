package FarmaciaERP.infrastucture.persistence.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;

@Embeddable
@Getter
public class FullNameEmb {

    @Column(name = "name", nullable = true, length = 100)
    private String nombres;

    @Column(name = "last_name", nullable = true, length = 100)
    private String apellidos;

    @Column(name = "full_name")
    @Getter(AccessLevel.NONE)
    private String valor;

    public FullNameEmb(String nombres, String apellidos) {
        this.nombres = nombres;
        this.apellidos = apellidos;

        valor = nombres +" "+  apellidos;
    }

    protected FullNameEmb() {}
}
