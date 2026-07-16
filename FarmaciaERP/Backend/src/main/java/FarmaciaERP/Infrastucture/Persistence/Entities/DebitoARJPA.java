package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoDebitoAR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DebitoAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebitoARJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receta_medica_ar_id", nullable = false, unique = true)
    private RecetaMedicaARJPA recetaMedicaAR;

    @Column(nullable = false)
    private double monto;

    @Column(length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDebitoAR estado;

    private Boolean justificado;

    @Column(nullable = false)
    private boolean tramitado;

    @Column(nullable = false)
    private boolean ajustado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private LocalDateTime fechaAjuste;
}
