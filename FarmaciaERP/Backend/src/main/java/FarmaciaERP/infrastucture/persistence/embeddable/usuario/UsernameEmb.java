package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;


@Embeddable
@Getter
public class UsernameEmb {
    @Column(name = "username", nullable = false, unique = true)
    private String valor;

    protected UsernameEmb() {
    }
    public UsernameEmb(String valor) {
        this.valor = valor;
    }
}
