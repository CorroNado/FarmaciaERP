package FarmaciaERP.Infrastucture.Persistence.Entities;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.DniEmb;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Cliente")
@Getter
@Setter
@NoArgsConstructor // Reemplaza al constructor vacío manual
@AllArgsConstructor // Reemplaza al constructor con parámetros manual
public class ClienteJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private FullNameEmb nombres;

    @Embedded
    private DniEmb dni;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "ESSALUD") en lugar del número
    @Column(name = "tipo_seguro", nullable = false)
    private TipoSeguro tipoSeguro;
}
