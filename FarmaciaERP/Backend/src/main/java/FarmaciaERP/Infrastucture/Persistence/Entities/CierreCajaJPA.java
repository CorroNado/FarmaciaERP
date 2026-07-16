package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoCierreCaja;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CierreCaja")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CierreCajaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private SucursalJPA sucursal;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private double reporteVentas;

    private Double arqueo;

    @Column(nullable = false)
    private double diferencia;

    private Boolean cuadra;

    @Column(length = 1000)
    private String justificacion;

    @Column(nullable = false)
    private boolean fisicosEnviados;

    @Column(nullable = false)
    private double copago;

    @Column(nullable = false)
    private double coberturaAseg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCierreCaja estado;
}
