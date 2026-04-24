package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.RecetaEstados;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Receta {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private String diagnostico;
    private List<DetalleReceta> detalles = new ArrayList<>();
    private String firma;
    private LocalDate fecha;
    private RecetaEstados  estado;

    public Receta() {
    }

    public Receta(Paciente paciente, Medico medico, String diagnostico,
                  List<DetalleReceta> detalles, LocalDate fecha) {

        estado = RecetaEstados.VIGENTE;
        this.paciente = paciente;
        this.medico = medico;
        this.diagnostico = diagnostico;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.fecha = fecha;
    }

    public Receta(Paciente paciente, Medico medico,List<DetalleReceta> detalles) {
        this.paciente = paciente;
        this.medico = medico;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }

    public void agregarDetalle(DetalleReceta detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle no puede ser null");
        }
        detalles.add(detalle);
    }
    public void quitarDetalle(DetalleReceta detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle no puede ser null");
        }
        detalles.remove(detalle);
    }
}
