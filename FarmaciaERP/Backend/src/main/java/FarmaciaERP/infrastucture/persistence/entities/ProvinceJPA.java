package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Province")
@Getter
@Setter
@NoArgsConstructor
public class ProvinceJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long provinciaId;

    @Column(nullable = false)
    private String nombre;

    @Embedded
    private UbigeoEmb ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartmentJPA departamento;

    public ProvinceJPA(Long provinciaId) {
        this.provinciaId = provinciaId;
    }

    public ProvinceJPA(String nombre, UbigeoEmb ubigeo, DepartmentJPA departamento) {
        this.nombre = nombre;
        this.ubigeo = ubigeo;
        this.departamento = departamento;
    }
}
