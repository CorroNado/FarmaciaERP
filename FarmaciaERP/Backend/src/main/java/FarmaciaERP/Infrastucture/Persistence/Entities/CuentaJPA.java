package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cuenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NaturalezaCuenta naturaleza;

    @Column(nullable = false)
    private boolean activa;
}