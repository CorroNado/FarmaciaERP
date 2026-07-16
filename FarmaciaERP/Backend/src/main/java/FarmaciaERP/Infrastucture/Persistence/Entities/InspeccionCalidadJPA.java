package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.DecisionQA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "InspeccionCalidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionCalidadJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrada_mercancia_id", nullable = false, unique = true)
    private EntradaMercanciaJPA entradaMercancia;

    @Column(name = "muestreo_conforme", nullable = false)
    private boolean muestreoConforme;

    @Column(name = "cadena_frio_conforme", nullable = false)
    private boolean cadenaFrioConforme;

    @Column(name = "registro_sanitario_vigente", nullable = false)
    private boolean registroSanitarioVigente;

    @Column(name = "empaque_conforme", nullable = false)
    private boolean empaqueConforme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionQA decision;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
