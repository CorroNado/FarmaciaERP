package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoAjusteContable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "AjusteContableRegularizacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AjusteContableRegularizacionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disputa_comercial_id", nullable = false, unique = true)
    private DisputaComercialJPA disputaComercial;

    @Column(name = "monto_regularizacion", nullable = false)
    private double montoRegularizacion;

    @Column(name = "recibe_nota_credito")
    private Boolean recibeNotaCredito;

    @Column(name = "reclamo_gestionado", nullable = false)
    private boolean reclamoGestionado;

    @Column(name = "nota_credito_enviada_proveedor", nullable = false)
    private boolean notaCreditoEnviadaProveedor;

    @Column(name = "nota_credito_registrada", nullable = false)
    private boolean notaCreditoRegistrada;

    @Column(name = "asiento_regularizacion", nullable = false)
    private boolean asientoRegularizacion;

    @Column(nullable = false)
    private boolean desbloqueado;

    @Column(nullable = false)
    private boolean regularizada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAjusteContable estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
