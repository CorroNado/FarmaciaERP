package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cotizacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJPA cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "vigencia_dias", nullable = false)
    private int vigenciaDias;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleCotizacionJPA> detalles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCotizacion estado;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;

    @Column(name = "venta_generada_id")
    private Long ventaGeneradaId;
}
