package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarRecetaMedicaARRequest {
    private String numero;
    private Long contabilizacionARId;
    private String medicamento;
    private String aseguradora;
    private double montoDeclarado;
    private double montoPreliquidado;
}
