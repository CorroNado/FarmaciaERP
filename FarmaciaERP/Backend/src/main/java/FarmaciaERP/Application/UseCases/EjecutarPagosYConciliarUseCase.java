package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.04 Fase 04 - paso 4.5: Ejecutar Pagos y Conciliar en SAP FI-AP
 * (Analista de Cuentas por Pagar). Concluye la Fase 04 a nivel de gestión
 * y habilita la Fase 05 (Procesamiento Automático y Propuesta de Pago).
 */
@Service
public class EjecutarPagosYConciliarUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public EjecutarPagosYConciliarUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(Long id) {
        LotePagoTesoreria lote = lotePagoTesoreriaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Lote de pagos no encontrado: " + id));

        lote.ejecutarPagosYConciliar();

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }
}
