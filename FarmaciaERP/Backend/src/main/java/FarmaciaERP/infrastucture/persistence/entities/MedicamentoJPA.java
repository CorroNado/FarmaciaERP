package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.MedicamentoCategoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Medicamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String presentacion;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private MedicamentoCategoria categoria;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
}
