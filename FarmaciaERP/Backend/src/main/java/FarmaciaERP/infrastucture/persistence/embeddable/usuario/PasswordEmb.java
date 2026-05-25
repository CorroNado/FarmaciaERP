package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class PasswordEmb {

    @Column(name = "password", nullable = false)
    private String value;

    protected PasswordEmb() {
    }

    public PasswordEmb(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}