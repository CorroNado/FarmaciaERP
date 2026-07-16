package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoDebitoAR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebitoARResponse {
    private Long id;
    private Long recetaMedicaARId;
    private String numeroReceta;
    private Long contabilizacionARId;
    private double monto;
    private String motivo;
    private EstadoDebitoAR estado;
    private Boolean justificado;
    private boolean tramitado;
    private boolean ajustado;
    private LocalDateTime fecha;
    private LocalDateTime fechaAjuste;
    private boolean conciliado;
}
