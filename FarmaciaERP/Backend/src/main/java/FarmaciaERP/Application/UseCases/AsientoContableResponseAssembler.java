package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Application.DTOs.Response.LineaAsientoResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;

import java.util.List;

public class AsientoContableResponseAssembler {

    public static AsientoContableResponse toResponse(AsientoContable asiento) {
        List<LineaAsientoResponse> lineasResponse = asiento.getLineas().stream()
                .map(AsientoContableResponseAssembler::toLineaResponse)
                .toList();

        return new AsientoContableResponse(
                asiento.getId(),
                asiento.getNumero(),
                asiento.getFechaContable(),
                asiento.getGlosa(),
                asiento.getTipoAsiento(),
                asiento.getEstado(),
                asiento.calcularTotalDebe(),
                asiento.calcularTotalHaber(),
                lineasResponse
        );
    }

    private static LineaAsientoResponse toLineaResponse(LineaAsiento linea) {
        return new LineaAsientoResponse(
                linea.getId(),
                linea.getSubcuenta().getId(),
                linea.getSubcuenta().getCodigo(),
                linea.getSubcuenta().getNombre(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getId(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getCodigo(),
                linea.getCentroCosto() == null ? null : linea.getCentroCosto().getNombre(),
                linea.getDebe(),
                linea.getHaber(),
                linea.getGlosaDetalle()
        );
    }
}