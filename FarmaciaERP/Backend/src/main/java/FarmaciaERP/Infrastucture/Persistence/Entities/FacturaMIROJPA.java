package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoFacturaMIRO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "FacturaMIRO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaMIROJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompraJPA ordenCompra;

    @Column(name = "fecha_emision", nullable = false)
    private String fechaEmision;

    @Column(name = "monto_neto", nullable = false)
    private double montoNeto;

    @Column(nullable = false)
    private double igv;

    @Column(name = "monto_total", nullable = false)
    private double montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFacturaMIRO estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
