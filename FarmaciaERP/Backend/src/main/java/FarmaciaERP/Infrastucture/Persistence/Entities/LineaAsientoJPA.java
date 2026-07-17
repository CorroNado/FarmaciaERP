package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "LineaAsiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaAsientoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asiento_contable_id", nullable = false)
    private AsientoContableJPA asientoContable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcuenta_id", nullable = false)
    private SubcuentaDivisionariaJPA subcuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_costo_id")
    private CentroCostoJPA centroCosto;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal debe;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal haber;

    @Column(length = 255)
    private String glosaDetalle;
}