package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.03 Fase 03 - paso 3.1.a (cont.): el laboratorio/droguería evalúa el
 * reclamo y envía la Nota de Crédito, quedando registrada en SAP.
 */
@Service
public class EvaluarYEnviarNotaCreditoUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public EvaluarYEnviarNotaCreditoUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    @Transactional
    public AjusteContableRegularizacionResponse ejecutar(Long id) {
        AjusteContableRegularizacion ajuste = ajusteContableRegularizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Ajuste contable no encontrado: " + id));

        ajuste.evaluarYEnviarNotaCredito();

        AjusteContableRegularizacion guardado = ajusteContableRegularizacionRepository.save(ajuste);
        return AjusteContableRegularizacionResponseAssembler.toResponse(guardado);
    }
}
