package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;


@Embeddable
@Getter
public class UsernameEmb {
    @Column(name = "username", nullable = false, length = 10, unique = true)
    private String value;

    protected UsernameEmb() {
    }
    public UsernameEmb(String value) {
        this.value = value;
    }
}
