package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;

/**
 * Ensambla el DTO de respuesta a partir de la entidad de dominio CierreCaja.
 * No es un caso de uso en si mismo, es utilitario compartido entre los casos de uso de CierreCaja.
 */
public class CierreCajaResponseAssembler {

    public static CierreCajaResponse toResponse(CierreCaja cierre) {
        return new CierreCajaResponse(
                cierre.getId(),
                cierre.getNumero(),
                cierre.getSucursal().getId(),
                cierre.getSucursal().getNombre(),
                cierre.getFecha(),
                cierre.getReporteVentas(),
                cierre.getArqueo(),
                cierre.getDiferencia(),
                cierre.getCuadra(),
                cierre.getJustificacion(),
                cierre.isFisicosEnviados(),
                cierre.getCopago(),
                cierre.getCoberturaAseg(),
                cierre.getEstado(),
                cierre.puedeContinuarFase02()
        );
    }
}
