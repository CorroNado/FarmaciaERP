package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Embeddable
public class EmailEmb {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    protected EmailEmb() {
    }
}
