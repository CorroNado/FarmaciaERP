package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DetallePlanillaEmpleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePlanillaEmpleadoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planilla_id", nullable = false)
    private PlanillaJPA planilla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_id", nullable = false)
    private EmpleadoJPA empleado;

    @Column(name = "sueldo_base", nullable = false)
    private double sueldoBase;

    @Column(name = "sueldo_diario", nullable = false)
    private double sueldoDiario;

    @Column(name = "dias_con_turno", nullable = false)
    private int diasConTurno;

    @Column(nullable = false)
    private int faltas;

    @Column(name = "minutos_tardanza", nullable = false)
    private int minutosTardanza;

    @Column(name = "horas_extras", nullable = false)
    private double horasExtras;

    @Column(name = "monto_extra", nullable = false)
    private double montoExtra;

    @Column(name = "descuento_faltas", nullable = false)
    private double descuentoFaltas;

    @Column(name = "descuento_tardanzas", nullable = false)
    private double descuentoTardanzas;

    @Column(name = "bono_asistencia", nullable = false)
    private double bonoAsistencia;

    @Column(name = "bono_cumplimiento", nullable = false)
    private double bonoCumplimiento;

    @Column(name = "bono_metas", nullable = false)
    private double bonoMetas;

    @Column(nullable = false)
    private double essalud;

    @Column(nullable = false)
    private double afp;

    @Column(name = "otros_descuentos", nullable = false)
    private double otrosDescuentos;

    @Column(name = "sueldo_neto", nullable = false)
    private double sueldoNeto;
}
