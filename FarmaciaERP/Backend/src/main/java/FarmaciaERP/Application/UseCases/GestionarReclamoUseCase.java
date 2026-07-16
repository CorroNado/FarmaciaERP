package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.03 Fase 03 - paso 3.1.a: Nota de Crédito no recibida — el Sistema
 * SAP ERP gestiona el reclamo directamente con el laboratorio/droguería.
 */
@Service
public class GestionarReclamoUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public GestionarReclamoUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    @Transactional
    public AjusteContableRegularizacionResponse ejecutar(Long id) {
        AjusteContableRegularizacion ajuste = ajusteContableRegularizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Ajuste contable no encontrado: " + id));

        ajuste.gestionarReclamo();

        AjusteContableRegularizacion guardado = ajusteContableRegularizacionRepository.save(ajuste);
        return AjusteContableRegularizacionResponseAssembler.toResponse(guardado);
    }
}
