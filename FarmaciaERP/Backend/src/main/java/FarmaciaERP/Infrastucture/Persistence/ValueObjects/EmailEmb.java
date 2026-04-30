package FarmaciaERP.Infrastucture.Persistence.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Embeddable
public class EmailEmb {
    private String email;

    protected EmailEmb() {
    }
}
