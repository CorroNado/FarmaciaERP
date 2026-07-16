package FarmaciaERP.Application.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SugerenciaMRPItemResponse {
    private int medicamentoId;
    private String nombreMedicamento;
    private int stockActual;
    private int stockMinimo;
    private int cantidadSugerida;
    private double precioUnitario;
    private boolean porDebajoDelMinimo;
}
