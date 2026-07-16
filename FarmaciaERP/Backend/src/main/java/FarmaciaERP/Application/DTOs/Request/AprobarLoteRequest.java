package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AprobarLoteRequest {
    private Long entradaMercanciaId;
    private boolean muestreoConforme;
    private boolean registroSanitarioVigente;
    private boolean empaqueConforme;
}
