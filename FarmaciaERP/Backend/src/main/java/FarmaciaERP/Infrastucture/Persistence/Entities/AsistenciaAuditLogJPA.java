package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.TipoMovimientoAsistencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "AsistenciaAuditLog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaAuditLogJPA {

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
    private TipoMovimientoAsistencia tipo;

    @Column(length = 1000)
    private String detalle;

    @Column(length = 2000)
    private String antes;

    @Column(length = 2000)
    private String despues;

    @Column(length = 1000)
    private String motivo;
}
