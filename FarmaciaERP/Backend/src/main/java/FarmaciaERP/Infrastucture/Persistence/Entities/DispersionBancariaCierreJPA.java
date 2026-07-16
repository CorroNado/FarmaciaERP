package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoDispersionCierre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DispersionBancariaCierre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DispersionBancariaCierreJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propuesta_pago_automatica_id", nullable = false, unique = true)
    private PropuestaPagoAutomaticaJPA propuestaPagoAutomatica;

    @Column(name = "monto_dispersion", nullable = false)
    private double montoDispersion;

    @Column(name = "propuesta_compilada", nullable = false)
    private boolean propuestaCompilada;

    @Column(name = "propuesta_validada")
    private Boolean propuestaValidada;

    @Column(name = "intentos_validacion", nullable = false)
    private int intentosValidacion;

    @Column(name = "lote_corregido", nullable = false)
    private boolean loteCorregido;

    @Column(name = "archivo_generado", nullable = false)
    private boolean archivoGenerado;

    @Column(nullable = false)
    private boolean firmado;

    @Column(name = "transferencias_ejecutadas", nullable = false)
    private boolean transferenciasEjecutadas;

    @Column(name = "extracto_importado", nullable = false)
    private boolean extractoImportado;

    @Column(nullable = false)
    private boolean conciliado;

    @Column(name = "obligacion_extinguida", nullable = false)
    private boolean obligacionExtinguida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDispersionCierre estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
