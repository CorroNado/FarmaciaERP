package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoOrdenTraslado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenTrasladoResponse {
    private Long id;
    private String numero;
    private Long inspeccionCalidadId;
    private String lote;
    private Long sucursalDestinoId;
    private String sucursalDestinoNombre;
    private String guiaRemision;
    private EstadoOrdenTraslado estado;
    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaRecepcion;
}
