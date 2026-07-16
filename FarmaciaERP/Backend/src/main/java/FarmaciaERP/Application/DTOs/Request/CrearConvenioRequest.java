package FarmaciaERP.Application.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearConvenioRequest {
    private String numero;
    private Long proveedorId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ItemConvenioRequest> itemsPactados;
}
