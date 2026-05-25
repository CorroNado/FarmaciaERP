package FarmaciaERP.infrastucture.persistence.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Embeddable
public class EmailAddressEmb {

    @Column(name = "direccion", nullable = false, unique = true)
    private String address;

    protected EmailAddressEmb() {
    }
}
