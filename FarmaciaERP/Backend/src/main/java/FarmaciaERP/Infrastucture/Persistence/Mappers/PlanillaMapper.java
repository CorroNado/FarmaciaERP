package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetallePlanillaEmpleado;
import FarmaciaERP.Domain.Entities.Planilla;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetallePlanillaEmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PlanillaJPA;

import java.util.List;
import java.util.Map;

public class PlanillaMapper {

    /**
     * `empleadosRefById` deben ser instancias YA GESTIONADAS por JPA,
     * indexadas por el id del colaborador de cada línea de detalle.
     */
    public static PlanillaJPA ToEntity(Planilla planilla, Map<Long, EmpleadoJPA> empleadosRefById) {
        PlanillaJPA entity = new PlanillaJPA();
        entity.setId(planilla.getId());
        entity.setMes(planilla.getMes());
        entity.setAnio(planilla.getAnio());
        entity.setFechaCalculo(planilla.getFechaCalculo());
        entity.setFechaGuardado(planilla.getFechaGuardado());

        List<DetallePlanillaEmpleadoJPA> detalles = planilla.getDetalles().stream()
                .map(detalle -> toDetalleEntity(detalle, entity, empleadosRefById.get(detalle.getEmpleado().getId())))
                .toList();
        entity.setDetalles(detalles);
        return entity;
    }

    private static DetallePlanillaEmpleadoJPA toDetalleEntity(DetallePlanillaEmpleado detalle, PlanillaJPA planillaRef,
                                                                EmpleadoJPA empleadoRef) {
        DetallePlanillaEmpleadoJPA entity = new DetallePlanillaEmpleadoJPA();
        entity.setId(detalle.getId());
        entity.setPlanilla(planillaRef);
        entity.setEmpleado(empleadoRef);
        entity.setSueldoBase(detalle.getSueldoBase());
        entity.setSueldoDiario(detalle.getSueldoDiario());
        entity.setDiasConTurno(detalle.getDiasConTurno());
        entity.setFaltas(detalle.getFaltas());
        entity.setMinutosTardanza(detalle.getMinutosTardanza());
        entity.setHorasExtras(detalle.getHorasExtras());
        entity.setMontoExtra(detalle.getMontoExtra());
        entity.setDescuentoFaltas(detalle.getDescuentoFaltas());
        entity.setDescuentoTardanzas(detalle.getDescuentoTardanzas());
        entity.setBonoAsistencia(detalle.getBonoAsistencia());
        entity.setBonoCumplimiento(detalle.getBonoCumplimiento());
        entity.setBonoMetas(detalle.getBonoMetas());
        entity.setEssalud(detalle.getEssalud());
        entity.setAfp(detalle.getAfp());
        entity.setOtrosDescuentos(detalle.getOtrosDescuentos());
        entity.setSueldoNeto(detalle.getSueldoNeto());
        return entity;
    }

    public static Planilla ToDomain(PlanillaJPA entity) {
        List<DetallePlanillaEmpleado> detalles = entity.getDetalles().stream()
                .map(PlanillaMapper::toDetalleDomain)
                .toList();
        return Planilla.reconstruir(entity.getId(), entity.getMes(), entity.getAnio(), detalles,
                entity.getFechaCalculo(), entity.getFechaGuardado());
    }

    private static DetallePlanillaEmpleado toDetalleDomain(DetallePlanillaEmpleadoJPA entity) {
        return DetallePlanillaEmpleado.reconstruir(
                entity.getId(),
                EmpleadoMapper.ToDomain(entity.getEmpleado()),
                entity.getSueldoBase(),
                entity.getSueldoDiario(),
                entity.getDiasConTurno(),
                entity.getFaltas(),
                entity.getMinutosTardanza(),
                entity.getHorasExtras(),
                entity.getMontoExtra(),
                entity.getDescuentoFaltas(),
                entity.getDescuentoTardanzas(),
                entity.getBonoAsistencia(),
                entity.getBonoCumplimiento(),
                entity.getBonoMetas(),
                entity.getEssalud(),
                entity.getAfp(),
                entity.getOtrosDescuentos(),
                entity.getSueldoNeto()
        );
    }
}
