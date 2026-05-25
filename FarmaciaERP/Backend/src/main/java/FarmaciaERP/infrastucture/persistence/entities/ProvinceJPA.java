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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private UbigeoEmb ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentJPA department;

    public ProvinceJPA(String name, UbigeoEmb ubigeo, DepartmentJPA department) {
        this.name = name;
        this.ubigeo = ubigeo;
        this.department = department;
    }
}
