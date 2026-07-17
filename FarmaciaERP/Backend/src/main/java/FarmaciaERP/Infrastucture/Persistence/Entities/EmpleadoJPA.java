package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Enums.RolEmpleado;
import FarmaciaERP.Domain.Enums.TipoContrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false, unique = true)
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolEmpleado rol;

    private String area;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(nullable = false)
    private double salario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContrato contrato;

    private String correo;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEmpleado estado;

    @Column(name = "baja_programada_fecha_efectiva")
    private LocalDateTime bajaProgramadaFechaEfectiva;

    @Column(name = "baja_programada_observacion")
    private String bajaProgramadaObservacion;

    @Column(name = "baja_programada_turno_info")
    private String bajaProgramadaTurnoInfo;
}
