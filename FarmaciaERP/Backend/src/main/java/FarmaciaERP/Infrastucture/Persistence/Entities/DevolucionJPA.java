package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.AccionDevolucion;
import FarmaciaERP.Domain.Enums.MotivoDevolucion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Devolucion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venta_id", nullable = false)
    private VentaJPA venta;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "devolucion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleDevolucionJPA> detalles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoDevolucion motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccionDevolucion accion;
}
