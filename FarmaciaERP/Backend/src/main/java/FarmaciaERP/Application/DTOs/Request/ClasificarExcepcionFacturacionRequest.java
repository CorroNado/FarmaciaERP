package FarmaciaERP.Application.DTOs.Request;

import FarmaciaERP.Domain.Enums.TipoDiscrepancia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClasificarExcepcionFacturacionRequest {
    private TipoDiscrepancia tipoDiscrepancia;
}
