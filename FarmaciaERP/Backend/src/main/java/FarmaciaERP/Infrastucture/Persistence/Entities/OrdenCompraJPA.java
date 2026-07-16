package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OrdenCompra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompraJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "solped_id", nullable = false)
    private SolicitudPedidoJPA solicitudPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private ProveedorJPA proveedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "convenio_id", nullable = false)
    private ConvenioJPA convenio;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleOrdenCompraJPA> detalles = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_entrega_limite")
    private String fechaEntregaLimite;

    @Column(name = "centro_destino")
    private String centroDestino;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrdenCompra estado;

    @Column(name = "fecha_firma")
    private LocalDateTime fechaFirma;
}
