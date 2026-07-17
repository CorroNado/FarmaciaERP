package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DetallePlanillaEmpleadoResponse;
import FarmaciaERP.Application.DTOs.Response.PlanillaResponse;
import FarmaciaERP.Application.DTOs.Response.PlanillaResumenResponse;
import FarmaciaERP.Domain.Entities.DetallePlanillaEmpleado;
import FarmaciaERP.Domain.Entities.Planilla;

public class PlanillaResponseAssembler {

    private static final String[] NOMBRES_MES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public static PlanillaResponse toResponse(Planilla planilla) {
        return new PlanillaResponse(
                planilla.getId(),
                planilla.getMes(),
                nombreMes(planilla.getMes()),
                planilla.getAnio(),
                planilla.estaGuardada(),
                planilla.getDetalles().stream().map(PlanillaResponseAssembler::toDetalle).toList(),
                planilla.getMontoTotalNeto(),
                planilla.getFechaCalculo(),
                planilla.getFechaGuardado()
        );
    }

    public static PlanillaResumenResponse toResumen(Planilla planilla) {
        return new PlanillaResumenResponse(
                planilla.getId(),
                planilla.getMes(),
                nombreMes(planilla.getMes()),
                planilla.getAnio(),
                planilla.getDetalles().size(),
                planilla.getMontoTotalNeto(),
                planilla.getFechaGuardado()
        );
    }

    private static DetallePlanillaEmpleadoResponse toDetalle(DetallePlanillaEmpleado detalle) {
        return new DetallePlanillaEmpleadoResponse(
                detalle.getEmpleado().getId(),
                detalle.getEmpleado().getCodigo(),
                detalle.getEmpleado().getNombreCompleto(),
                detalle.getSueldoBase(),
                detalle.getSueldoDiario(),
                detalle.getDiasConTurno(),
                detalle.getFaltas(),
                detalle.getMinutosTardanza(),
                detalle.getHorasExtras(),
                detalle.getMontoExtra(),
                detalle.getDescuentoFaltas(),
                detalle.getDescuentoTardanzas(),
                detalle.getBonoAsistencia(),
                detalle.getBonoCumplimiento(),
                detalle.getBonoMetas(),
                detalle.getEssalud(),
                detalle.getAfp(),
                detalle.getOtrosDescuentos(),
                detalle.getSueldoNeto()
        );
    }

    private static String nombreMes(int mes) {
        if (mes < 1 || mes > 12) {
            return "";
        }
        return NOMBRES_MES[mes - 1];
    }
}
