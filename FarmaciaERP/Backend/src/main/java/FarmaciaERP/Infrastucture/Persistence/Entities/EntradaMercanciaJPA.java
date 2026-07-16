package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoEntradaMercancia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "EntradaMercancia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntradaMercanciaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompraJPA ordenCompra;

    @Column(nullable = false)
    private String lote;

    @Column(name = "fecha_vencimiento", nullable = false)
    private String fechaVencimiento;

    @Column(name = "temperatura_arribo", nullable = false)
    private double temperaturaArribo;

    @Column(name = "cantidad_pedida", nullable = false)
    private int cantidadPedida;

    @Column(name = "cantidad_recibida", nullable = false)
    private int cantidadRecibida;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEntradaMercancia estado;

    @Column(name = "alerta_cadena_frio", nullable = false)
    private boolean alertaCadenaFrio;
}
