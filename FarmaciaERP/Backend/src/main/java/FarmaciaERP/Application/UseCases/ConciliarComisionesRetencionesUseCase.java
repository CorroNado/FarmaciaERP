package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ConciliarComisionesRetencionesRequest;
import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 05 - 5.2 Conciliar Comisiones de Tarjetas y Retenciones
 * (RN-AR5-01), realizada por el Analista AR sobre el monto ya
 * interpretado del archivo de transferencia.
 */
@Service
public class ConciliarComisionesRetencionesUseCase {

    private final ICobroARRepository cobroARRepository;

    public ConciliarComisionesRetencionesUseCase(ICobroARRepository cobroARRepository) {
        this.cobroARRepository = cobroARRepository;
    }

    @Transactional
    public CobroARResponse ejecutar(Long id, ConciliarComisionesRetencionesRequest request) {
        CobroAR cobro = cobroARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cobro AR no encontrado: " + id));

        cobro.conciliarComisionesRetenciones(request.getComisionPct());

        CobroAR guardado = cobroARRepository.save(cobro);
        return CobroARResponseAssembler.toResponse(guardado);
    }
}
