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
@Table(name = "Cliente")
@Getter
@Setter
@NoArgsConstructor // Reemplaza al constructor vac├¡o manual
@AllArgsConstructor // Reemplaza al constructor con par├ímetros manual
public class ClienteJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FullNameEmb nombres;

    @Embedded
    private DniEmb dni;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "ESSALUD") en lugar del n├║mero
    @Column(name = "tipo_seguro", nullable = false)
    private InsuranceType InsuranceType;
}
