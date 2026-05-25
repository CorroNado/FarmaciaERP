package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "District")
@AllArgsConstructor
@Getter
@Setter
public class DistrictJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Embedded
    private UbigeoEmb ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private ProvinceJPA province;

    protected DistrictJPA() {}

    public DistrictJPA(String nombre, UbigeoEmb ubigeo, ProvinceJPA province) {
        this.nombre = nombre;
        this.ubigeo = ubigeo;
        this.province = province;
    }
}
