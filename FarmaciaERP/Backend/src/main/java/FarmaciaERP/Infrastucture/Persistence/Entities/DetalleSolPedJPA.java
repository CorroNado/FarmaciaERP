package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DetalleSolPed")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleSolPedJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "solped_id", nullable = false)
    private SolicitudPedidoJPA solicitudPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private MedicamentoJPA medicamento;

    @Column(name = "stock_actual", nullable = false)
    private int stockActual;

    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo;

    @Column(name = "cantidad_sugerida", nullable = false)
    private int cantidadSugerida;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;
}
