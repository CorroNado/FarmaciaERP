package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - Corregir Lote según Observaciones del Comité Semanal
 * de Tesorería (Analista de Cuentas por Pagar).
 */
@Service
public class CorregirLoteUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public CorregirLoteUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.corregirLoteSegunObservaciones();

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
