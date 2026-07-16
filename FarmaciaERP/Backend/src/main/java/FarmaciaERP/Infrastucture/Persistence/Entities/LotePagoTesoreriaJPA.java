package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoLotePago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LotePagoTesoreria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotePagoTesoreriaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "LotePagoAjusteContable",
            joinColumns = @JoinColumn(name = "lote_pago_id"),
            inverseJoinColumns = @JoinColumn(name = "ajuste_contable_id")
    )
    private List<AjusteContableRegularizacionJPA> ajustesContables = new ArrayList<>();

    @Column(name = "monto_neto_regularizado", nullable = false)
    private double montoNetoRegularizado;

    @Column(name = "proveedores_criticos_priorizados", nullable = false)
    private boolean proveedoresCriticosPriorizados;

    @Column(name = "descuento_pronto_pago_negociado", nullable = false)
    private boolean descuentoProntoPagoNegociado;

    @Column(name = "descuento_pronto_pago_pct", nullable = false)
    private double descuentoProntoPagoPct;

    @Column(name = "lote_preparado", nullable = false)
    private boolean lotePreparado;

    @Column(name = "monto_lote", nullable = false)
    private double montoLote;

    @Column(name = "fondos_verificados", nullable = false)
    private boolean fondosVerificados;

    @Column(name = "revisiones_comite", nullable = false)
    private int revisionesComite;

    @Column(name = "lote_corregido", nullable = false)
    private boolean loteCorregido;

    @Column(name = "aprobado_por_comite", nullable = false)
    private boolean aprobadoPorComite;

    @Column(name = "pagos_conciliados_gestion", nullable = false)
    private boolean pagosConciliadosGestion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLotePago estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
