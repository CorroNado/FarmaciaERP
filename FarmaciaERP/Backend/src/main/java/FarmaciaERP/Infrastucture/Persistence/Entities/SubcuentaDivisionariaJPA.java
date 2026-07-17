package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SubcuentaDivisionaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcuentaDivisionariaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CuentaJPA cuenta;

    @Column(nullable = false)
    private boolean activa;
}