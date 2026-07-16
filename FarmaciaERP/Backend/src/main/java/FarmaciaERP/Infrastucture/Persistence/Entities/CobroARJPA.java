package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoCobroAR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CobroAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CobroARJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contabilizacion_ar_id", nullable = false, unique = true)
    private ContabilizacionARJPA contabilizacionAR;

    @Column(nullable = false)
    private double montoTransferido;

    @Column(nullable = false)
    private double retenciones;

    @Column(nullable = false)
    private double comisionPct;

    @Column(nullable = false)
    private double montoConciliado;

    @Column(nullable = false)
    private double diferencia;

    private Boolean cuadra;

    @Column(nullable = false)
    private boolean registrado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCobroAR estado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private LocalDateTime fechaRegistro;
}
