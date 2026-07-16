package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoRecetaAR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "RecetaMedicaAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetaMedicaARJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contabilizacion_ar_id", nullable = false)
    private ContabilizacionARJPA contabilizacionAR;

    @Column(nullable = false)
    private String medicamento;

    @Column(nullable = false)
    private String aseguradora;

    @Column(nullable = false)
    private double montoDeclarado;

    @Column(nullable = false)
    private double montoPreliquidado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRecetaAR estado;

    @Column(length = 500)
    private String motivoRechazo;

    @Column(length = 500)
    private String inconsistencia;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
