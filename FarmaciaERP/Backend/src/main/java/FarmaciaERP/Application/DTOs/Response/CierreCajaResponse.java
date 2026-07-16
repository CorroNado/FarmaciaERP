package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoCierreCaja;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CierreCajaResponse {
    private Long id;
    private String numero;
    private Long sucursalId;
    private String sucursalNombre;
    private LocalDateTime fecha;
    private double reporteVentas;
    private Double arqueo;
    private double diferencia;
    private Boolean cuadra;
    private String justificacion;
    private boolean fisicosEnviados;
    private double copago;
    private double coberturaAseg;
    private EstadoCierreCaja estado;
    private boolean puedeContinuarFase02;
}
