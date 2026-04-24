package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Entities.Receta;
import lombok.Data;

@Data
public class ValidarRecetaRequest {
    private int idreceta;
}
