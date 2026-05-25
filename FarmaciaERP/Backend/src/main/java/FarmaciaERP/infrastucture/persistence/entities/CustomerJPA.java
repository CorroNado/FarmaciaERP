package FarmaciaERP.infrastucture.persistence.entities;
import FarmaciaERP.domain.enums.InsuranceType;
import FarmaciaERP.infrastucture.persistence.embeddable.DniEmb;
import FarmaciaERP.infrastucture.persistence.embeddable.FullNameEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor // Reemplaza al constructor vacío manual
@AllArgsConstructor // Reemplaza al constructor con parámetros manual
public class CustomerJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FullNameEmb nombres;

    @Embedded
    private DniEmb dni;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "ESSALUD") en lugar del número
    @Column(name = "tipo_seguro", nullable = false)
    private InsuranceType insuranceType;
}
