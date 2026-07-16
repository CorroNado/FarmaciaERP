package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoSolPed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SolicitudPedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPedidoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String responsable;

    @Column(name = "centro_costo")
    private String centroCosto;

    @Column(nullable = false)
    private double presupuesto;

    @OneToMany(mappedBy = "solicitudPedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleSolPedJPA> detalles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolPed estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    private ProveedorJPA proveedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "convenio_id")
    private ConvenioJPA convenio;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;
}
