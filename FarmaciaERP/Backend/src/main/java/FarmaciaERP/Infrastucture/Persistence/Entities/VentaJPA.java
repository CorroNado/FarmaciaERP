package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Enums.MetodoPago;
import FarmaciaERP.Domain.Enums.TipoComprobante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJPA cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVentaJPA> detalles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false)
    private TipoComprobante tipoComprobante;

    @Column(name = "numero_comprobante")
    private String numeroComprobante;
}
