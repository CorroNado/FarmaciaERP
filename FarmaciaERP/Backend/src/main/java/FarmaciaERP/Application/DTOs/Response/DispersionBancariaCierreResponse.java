package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoDispersionCierre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DispersionBancariaCierreResponse {
    private Long id;
    private String numero;
    private Long propuestaPagoAutomaticaId;
    private String numeroPropuestaPago;
    private double montoDispersion;
    private boolean propuestaCompilada;
    private Boolean propuestaValidada;
    private int intentosValidacion;
    private boolean loteCorregido;
    private boolean archivoGenerado;
    private boolean firmado;
    private boolean transferenciasEjecutadas;
    private boolean extractoImportado;
    private boolean conciliado;
    private boolean obligacionExtinguida;
    private EstadoDispersionCierre estado;
    private LocalDateTime fecha;
}
