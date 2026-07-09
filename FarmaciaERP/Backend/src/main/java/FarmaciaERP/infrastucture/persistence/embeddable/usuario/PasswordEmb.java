package FarmaciaERP.infrastucture.persistence.embeddable.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class PasswordEmb {

    @Column(name = "password", nullable = false)
    private String valor;

    protected PasswordEmb() {
    }

    public PasswordEmb(String valor) {
        this.valor = valor;
    }
}