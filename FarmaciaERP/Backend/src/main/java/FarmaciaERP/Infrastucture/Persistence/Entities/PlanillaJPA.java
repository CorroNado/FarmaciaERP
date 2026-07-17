package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Planilla", uniqueConstraints = @UniqueConstraint(columnNames = {"mes", "anio"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanillaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int anio;

    @OneToMany(mappedBy = "planilla", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePlanillaEmpleadoJPA> detalles = new ArrayList<>();

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado;
}
