package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoCompensacionAR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CompensacionAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompensacionARJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contabilizacion_ar_id", nullable = false, unique = true)
    private ContabilizacionARJPA contabilizacionAR;

    @Column(nullable = false)
    private boolean compensado;

    @Column(nullable = false)
    private boolean reporteGenerado;

    @Column(nullable = false)
    private double montoVentas;

    @Column(nullable = false)
    private double montoAprobadas;

    @Column(nullable = false)
    private double perdidas;

    @Column(nullable = false)
    private double margenNeto;

    @Column(nullable = false)
    private double margenPct;

    @Column(nullable = false)
    private boolean saldoConfirmado;

    @Column(nullable = false)
    private boolean cerrado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCompensacionAR estado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private LocalDateTime fechaCierre;
}
