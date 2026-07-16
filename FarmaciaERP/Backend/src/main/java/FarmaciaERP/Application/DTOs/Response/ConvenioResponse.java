package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoConvenio;
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
public class ConvenioResponse {
    private Long id;
    private String numero;
    private Long proveedorId;
    private String razonSocialProveedor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoConvenio estado;
    private boolean vigente;
    private List<ItemConvenioResponse> itemsPactados;
}
