package FarmaciaERP.Application.DTOs.Response;

import FarmaciaERP.Domain.Enums.EstadoPropuestaPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaPagoAutomaticaResponse {
    private Long id;
    private String numero;
    private Long lotePagoTesoreriaId;
    private String numeroLotePago;
    private double montoPropuesta;
    private String sociedad;
    private String viaPago;
    private String fechaPago;
    private boolean parametrosIntroducidos;
    private boolean propuestaEjecutada;
    private int intentos;
    private Boolean propuestaCorrecta;
    private boolean propuestaAprobada;
    private boolean pagoEjecutado;
    private boolean archivosGenerados;
    private EstadoPropuestaPago estado;
    private LocalDateTime fecha;
}
