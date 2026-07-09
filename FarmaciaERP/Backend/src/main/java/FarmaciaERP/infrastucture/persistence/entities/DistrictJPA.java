package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "District")
@AllArgsConstructor
@Getter
@Setter
public class DistrictJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long distritoId;

    @Column(nullable = false)
    private String nombre;

    @Embedded
    private UbigeoEmb ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private ProvinceJPA provincia;

    public DistrictJPA() {}

    public DistrictJPA(String nombre, UbigeoEmb ubigeo, ProvinceJPA provincia) {
        this.nombre = nombre;
        this.ubigeo = ubigeo;
        this.provincia = provincia;
    }
    public DistrictJPA(Long distritoId) {
        this.distritoId = distritoId;
        // Distrito de Referencia
    }
}
