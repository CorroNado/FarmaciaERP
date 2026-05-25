package FarmaciaERP.infrastucture.persistence.embeddable;

import FarmaciaERP.domain.enums.UbigeoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UbigeoEmb {

    @Column(name = "ubigeo_reniec", nullable = false)
    private String ubigeoReniec;

    @Column(name = "ubigeo_inei", nullable = false)
    private String ubigeoInei;

    @Enumerated(EnumType.STRING)
    @Column(name = "ubigeo_type", nullable = false)
    private UbigeoType type;

    public UbigeoEmb(String ubigeoReniec, String ubigeoInei, UbigeoType type) {
        this.ubigeoReniec = ubigeoReniec;
        this.ubigeoInei = ubigeoInei;
        this.type = type;
    }
}