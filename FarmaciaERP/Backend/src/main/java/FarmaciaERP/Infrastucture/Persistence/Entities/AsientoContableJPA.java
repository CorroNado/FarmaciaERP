package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsiento;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AsientoContable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsientoContableJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String numero;

    @Column(nullable = false)
    private LocalDate fechaContable;

    @Column(nullable = false, length = 500)
    private String glosa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAsiento tipoAsiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsiento estado;

    @OneToMany(mappedBy = "asientoContable", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LineaAsientoJPA> lineas = new ArrayList<>();
}