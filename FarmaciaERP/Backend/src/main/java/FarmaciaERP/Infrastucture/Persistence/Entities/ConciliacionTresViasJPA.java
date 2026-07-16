package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.ResultadoConciliacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConciliacionTresVias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConciliacionTresViasJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompraJPA ordenCompra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrada_mercancia_id", nullable = false)
    private EntradaMercanciaJPA entradaMercancia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factura_miro_id", nullable = false)
    private FacturaMIROJPA facturaMIRO;

    @Column(name = "cantidad_coincide", nullable = false)
    private boolean cantidadCoincide;

    @Column(name = "precio_coincide", nullable = false)
    private boolean precioCoincide;

    @Column(name = "factura_vinculada", nullable = false)
    private boolean facturaVinculada;

    @Column(name = "qa_aprobado", nullable = false)
    private boolean qaAprobado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoConciliacion resultado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
