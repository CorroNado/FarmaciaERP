package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factura_miro_id", nullable = false, unique = true)
    private FacturaMIROJPA facturaMIRO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conciliacion_tres_vias_id", nullable = false)
    private ConciliacionTresViasJPA conciliacionTresVias;

    @Column(nullable = false)
    private String banco;

    @Column(name = "fecha_pago", nullable = false)
    private String fechaPago;

    @Column(nullable = false)
    private double monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
