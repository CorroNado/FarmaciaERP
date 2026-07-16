package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoOrdenTraslado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrdenTraslado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrasladoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inspeccion_calidad_id", nullable = false, unique = true)
    private InspeccionCalidadJPA inspeccionCalidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_destino_id", nullable = false)
    private SucursalJPA sucursalDestino;

    @Column(name = "guia_remision", nullable = false)
    private String guiaRemision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrdenTraslado estado;

    @Column(name = "fecha_despacho", nullable = false)
    private LocalDateTime fechaDespacho;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;
}
