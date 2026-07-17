package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CentroCosto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CentroCostoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    private SucursalJPA sucursal;

    @Column(nullable = false)
    private boolean activo;
}