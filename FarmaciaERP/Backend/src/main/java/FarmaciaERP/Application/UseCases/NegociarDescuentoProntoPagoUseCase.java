package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.NegociarDescuentoProntoPagoRequest;
import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - paso 4.2: Negociar Descuento por Pronto Pago (Gerente
 * de Tesorería).
 */
@Service
public class NegociarDescuentoProntoPagoUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public NegociarDescuentoProntoPagoUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id, NegociarDescuentoProntoPagoRequest request) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.negociarDescuentoProntoPago(request.getDescuentoPct());

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
