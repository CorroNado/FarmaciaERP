package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ConvenioResponse;
import FarmaciaERP.Application.DTOs.Response.ItemConvenioResponse;
import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Domain.Entities.ItemConvenio;

import java.util.List;

public class ConvenioResponseAssembler {

    public static ConvenioResponse toResponse(Convenio convenio) {
        List<ItemConvenioResponse> items = convenio.getItemsPactados().stream()
                .map(ConvenioResponseAssembler::toItemResponse)
                .toList();

        return new ConvenioResponse(
                convenio.getId(),
                convenio.getNumero(),
                convenio.getProveedor().getId(),
                convenio.getProveedor().getRazonSocial(),
                convenio.getFechaInicio(),
                convenio.getFechaFin(),
                convenio.getEstado(),
                convenio.estaVigente(),
                items
        );
    }

    private static ItemConvenioResponse toItemResponse(ItemConvenio item) {
        return new ItemConvenioResponse(
                item.getMedicamento().getId(),
                item.getMedicamento().getNombre(),
                item.getPrecioPactado()
        );
    }
}
