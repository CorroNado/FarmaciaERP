package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoContabilizacionAR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ContabilizacionAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContabilizacionARJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cierre_caja_id", nullable = false, unique = true)
    private CierreCajaJPA cierreCaja;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private boolean conciliacionPOS;

    @Column(nullable = false)
    private boolean asientoProcesado;

    @Column(nullable = false)
    private boolean ajusteDescuadre;

    @Column(nullable = false)
    private boolean recetasAuditadas;

    private Boolean recetasCorrectas;

    @Column(length = 1000)
    private String motivoObservacion;

    @Column(nullable = false)
    private boolean subsanacion;

    @Column(nullable = false)
    private boolean consolidado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContabilizacionAR estado;
}
