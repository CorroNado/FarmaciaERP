package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoCobroAR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CobroARResponse {
    private Long id;
    private Long contabilizacionARId;
    private double montoTransferido;
    private double retenciones;
    private double comisionPct;
    private double montoConciliado;
    private double diferencia;
    private Boolean cuadra;
    private boolean registrado;
    private EstadoCobroAR estado;
    private LocalDateTime fecha;
    private LocalDateTime fechaRegistro;
    private boolean puedeContinuarFase06;
}
