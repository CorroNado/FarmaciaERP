package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - ¿Lote Aprobado? Se somete el lote al Comité Semanal
 * de Tesorería. La primera revisión queda con observaciones; tras
 * corregirse, una nueva sumisión aprueba el lote.
 */
@Service
public class SometerLoteAComiteUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public SometerLoteAComiteUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.someterAComite();

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
