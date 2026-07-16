package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AjusteContableRegularizacionResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.03 Fase 03 - paso 3.3: Desbloquear Partida Presupuestaria / Factura
 * en MRBR y actualizar el estado de pago. RN-AP3-08: concluye la Fase 03 y
 * habilita la Fase 04 (Estrategia de Tesorería y Riesgo Sanitario).
 */
@Service
public class DesbloquearPartidaUseCase {

    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public DesbloquearPartidaUseCase(IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    @Transactional
    public AjusteContableRegularizacionResponse ejecutar(Long id) {
        AjusteContableRegularizacion ajuste = ajusteContableRegularizacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Ajuste contable no encontrado: " + id));

        ajuste.desbloquearPartida();

        AjusteContableRegularizacion guardado = ajusteContableRegularizacionRepository.save(ajuste);
        return AjusteContableRegularizacionResponseAssembler.toResponse(guardado);
    }
}
