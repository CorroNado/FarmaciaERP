package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.infrastucture.persistence.embeddable.UbigeoEmb;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Department")
@AllArgsConstructor
@Getter
@Setter
public class DepartmentJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departamentoId;

    @Column(nullable = false)
    private String nombre;

    @Embedded
    private UbigeoEmb ubigeo;

    public DepartmentJPA() {}

    public DepartmentJPA(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public DepartmentJPA(String nombre, UbigeoEmb ubigeo) {
        this.nombre = nombre;
        this.ubigeo = ubigeo;
    }
}
