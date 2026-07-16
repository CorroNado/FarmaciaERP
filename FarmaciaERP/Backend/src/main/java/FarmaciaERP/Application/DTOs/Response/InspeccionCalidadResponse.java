package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.DecisionQA;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspeccionCalidadResponse {
    private Long id;
    private String numero;
    private Long entradaMercanciaId;
    private String numeroEntradaMercancia;
    private String lote;
    private boolean muestreoConforme;
    private boolean cadenaFrioConforme;
    private boolean registroSanitarioVigente;
    private boolean empaqueConforme;
    private DecisionQA decision;
    private String motivoRechazo;
    private LocalDateTime fecha;
}
