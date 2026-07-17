package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.TipoMovimientoRRHH;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "EmpleadoAuditLog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoAuditLogJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String colaborador;

    @Column(name = "codigo_empleado", nullable = false)
    private String codigoEmpleado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimientoRRHH tipo;

    @Column(length = 1000)
    private String detalle;

    @Column(length = 2000)
    private String antes;

    @Column(length = 2000)
    private String despues;

    @Column(length = 1000)
    private String motivo;
}
