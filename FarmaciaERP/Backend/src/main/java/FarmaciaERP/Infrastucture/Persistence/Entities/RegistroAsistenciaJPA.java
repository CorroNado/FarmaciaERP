package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoAsistencia;
import FarmaciaERP.Domain.Enums.TurnoAsistencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "RegistroAsistencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAsistenciaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_id", nullable = false)
    private EmpleadoJPA empleado;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TurnoAsistencia turno;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "checkin_timestamp")
    private LocalDateTime checkinTimestamp;

    @Column(name = "horas_trabajadas")
    private Double horasTrabajadas;

    @Column(name = "horas_extras")
    private Double horasExtras;

    @Column(name = "factor_extra")
    private Double factorExtra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsistencia estado;

    @Column(nullable = false)
    private boolean registrado;

    @Column(nullable = false)
    private boolean justificado;

    @Column(name = "motivo_justificacion", length = 1000)
    private String motivoJustificacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
