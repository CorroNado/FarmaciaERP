package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.03 Fase 03 - paso 3.1.b: Nota de Crédito recibida — se registra
 * directamente en SAP.
 */
@Service
public class RegistrarNotaCreditoUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public RegistrarNotaCreditoUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    @Transactional
    public AjusteContableRegularizacionResponse ejecutar(Long id) {
        AjusteContableRegularizacion ajuste = ajusteContableRegularizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Ajuste contable no encontrado: " + id));

        ajuste.registrarNotaCredito();

        AjusteContableRegularizacion guardado = ajusteContableRegularizacionRepository.save(ajuste);
        return AjusteContableRegularizacionResponseAssembler.toResponse(guardado);
    }
}
