package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - paso 4.3: Preparar Lote de Pagos (F110 SAP), aplicando
 * el descuento por pronto pago sobre el monto neto regularizado.
 */
@Service
public class PrepararLotePagosUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public PrepararLotePagosUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.prepararLotePagos();

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
