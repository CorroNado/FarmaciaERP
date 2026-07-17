package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PartidaPresupuestal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaPresupuestalJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_costo_id", nullable = false)
    private CentroCostoJPA centroCosto;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal montoPresupuestado;

    @Column(nullable = false)
    private boolean activa;
}