package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - paso 4.1: Priorizar Proveedores Críticos de
 * Medicamentos (Analista de Cuentas por Pagar).
 */
@Service
public class PriorizarProveedoresCriticosUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public PriorizarProveedoresCriticosUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.priorizarProveedoresCriticos();

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
