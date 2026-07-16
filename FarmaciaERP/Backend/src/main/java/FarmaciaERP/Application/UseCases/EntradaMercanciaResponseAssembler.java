package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.EntradaMercanciaResponse;
import FarmaciaERP.Domain.Entities.EntradaMercancia;

public class EntradaMercanciaResponseAssembler {

    public static EntradaMercanciaResponse toResponse(EntradaMercancia entrada) {
        return new EntradaMercanciaResponse(
                entrada.getId(),
                entrada.getNumero(),
                entrada.getOrdenCompra().getId(),
                entrada.getOrdenCompra().getNumero(),
                entrada.getOrdenCompra().getProveedor().getRazonSocial(),
                entrada.getLote(),
                entrada.getFechaVencimiento(),
                entrada.getTemperaturaArribo(),
                entrada.getCantidadPedida(),
                entrada.getCantidadRecibida(),
                entrada.getDiferencia(),
                entrada.getPorcentajeDiferencia(),
                entrada.getEstado(),
                entrada.isAlertaCadenaFrio(),
                entrada.getFecha()
        );
    }
}
