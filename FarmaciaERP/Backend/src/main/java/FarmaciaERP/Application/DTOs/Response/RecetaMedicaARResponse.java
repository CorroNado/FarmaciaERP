package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoRecetaAR;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecetaMedicaARResponse {
    private Long id;
    private String numero;
    private Long contabilizacionARId;
    private String medicamento;
    private String aseguradora;
    private double montoDeclarado;
    private double montoPreliquidado;
    private EstadoRecetaAR estado;
    private String motivoRechazo;
    private String inconsistencia;
    private LocalDateTime fecha;
    private boolean procesada;
    private boolean generaDebito;
}
