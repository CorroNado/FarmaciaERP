package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.SugerenciaMRPItemRequest;
import FarmaciaERP.Application.DTOs.Request.SugerenciaMRPRequest;
import FarmaciaERP.Application.DTOs.Response.SugerenciaMRPItemResponse;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LOG.01 Fase 01 - RN-E1-001 / RN-E1-002: el sistema calcula la necesidad de
 * abastecimiento comparando el stock actual del medicamento contra el stock
 * mínimo de seguridad indicado por planeamiento.
 */
@Service
public class GenerarSugerenciaMRPUseCase {

    private final IMedicamentoRepository medicamentoRepository;

    public GenerarSugerenciaMRPUseCase(IMedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<SugerenciaMRPItemResponse> ejecutar(SugerenciaMRPRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Debe indicar al menos un medicamento para calcular el MRP");
        }

        return request.getItems().stream()
                .map(this::calcularSugerencia)
                .toList();
    }

    private SugerenciaMRPItemResponse calcularSugerencia(SugerenciaMRPItemRequest itemRequest) {
        Medicamento medicamento = medicamentoRepository.findById(itemRequest.getMedicamentoId())
                .orElseThrow(() -> new BadRequestException("Medicamento no encontrado: " + itemRequest.getMedicamentoId()));

        int stockActual = medicamento.getStock();
        int stockMinimo = itemRequest.getStockMinimo();
        // RN-E1-001: la cantidad sugerida repone hasta el doble del stock mínimo.
        int cantidadSugerida = Math.max((stockMinimo * 2) - stockActual, 0);

        return new SugerenciaMRPItemResponse(
                medicamento.getId(),
                medicamento.getNombre(),
                stockActual,
                stockMinimo,
                cantidadSugerida,
                medicamento.getPrecio(),
                stockActual < stockMinimo
        );
    }
}
