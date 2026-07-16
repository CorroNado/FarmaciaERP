package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoExcepcionFacturacion;
import FarmaciaERP.Domain.Enums.TipoDiscrepancia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExcepcionFacturacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcepcionFacturacionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conciliacion_tres_vias_id", nullable = false, unique = true)
    private ConciliacionTresViasJPA conciliacionTresVias;

    @Column(name = "monto_factura", nullable = false)
    private double montoFactura;

    @Column(name = "monto_contrato", nullable = false)
    private double montoContrato;

    @Column(nullable = false)
    private double diferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_discrepancia")
    private TipoDiscrepancia tipoDiscrepancia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoExcepcionFacturacion estado;

    @Column(nullable = false)
    private boolean revisada;

    @Column(nullable = false)
    private boolean clasificada;

    @Column(nullable = false)
    private boolean notificada;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
