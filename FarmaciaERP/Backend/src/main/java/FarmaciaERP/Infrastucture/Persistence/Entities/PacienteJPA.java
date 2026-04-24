package FarmaciaERP.Infrastucture.Persistence.Entities;
import FarmaciaERP.Domain.Enums.TipoSeguro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor // Reemplaza al constructor vacío manual
@AllArgsConstructor // Reemplaza al constructor con parámetros manual
public class PacienteJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Mapeamos 'dni' a la columna 'documento_identidad' para que coincida con tu IPacienteRepository
    @Column(name = "documento_identidad", nullable = false, unique = true, length = 20)
    private String dni;

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum (ej: "ESSALUD") en lugar del número
    @Column(name = "tipo_seguro", nullable = false)
    private TipoSeguro tipoSeguro;
}
