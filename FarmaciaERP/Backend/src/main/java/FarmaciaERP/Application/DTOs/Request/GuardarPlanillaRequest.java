package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuardarPlanillaRequest {
    /** 1 = Enero ... 12 = Diciembre. */
    private int mes;
    private int anio;
    /** Bono por metas editado manualmente por colaborador (opcional; por defecto 0). */
    private List<BonoMetasEmpleadoRequest> bonosMetas;
    /** Debe ser true si ya existe una planilla guardada para el mismo mes/año (confirma sobrescritura). */
    private boolean confirmarSobrescritura;
}
