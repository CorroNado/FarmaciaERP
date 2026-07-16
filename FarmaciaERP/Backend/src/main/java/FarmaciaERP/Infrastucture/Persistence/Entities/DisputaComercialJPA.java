package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoDisputaComercial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DisputaComercial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisputaComercialJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "excepcion_facturacion_id", nullable = false, unique = true)
    private ExcepcionFacturacionJPA excepcionFacturacion;

    @Column(nullable = false)
    private boolean cotejada;

    @Column(nullable = false)
    private boolean cuantificada;

    @Column(name = "impacto_financiero", nullable = false)
    private double impactoFinanciero;

    @Column(name = "validada_desviacion", nullable = false)
    private boolean validadaDesviacion;

    @Column(name = "disputa_abierta", nullable = false)
    private boolean disputaAbierta;

    @Column(name = "ronda_negociacion", nullable = false)
    private int rondaNegociacion;

    @Column(name = "monto_contraoferta", nullable = false)
    private double montoContraoferta;

    @Column(name = "absorbe_aceptado")
    private Boolean absorbeAceptado;

    @Column(name = "resuelta_workflow", nullable = false)
    private boolean resueltaWorkflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDisputaComercial estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
