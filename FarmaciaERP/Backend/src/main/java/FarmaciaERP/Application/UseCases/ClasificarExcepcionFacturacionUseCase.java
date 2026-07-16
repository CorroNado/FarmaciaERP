package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ClasificarExcepcionFacturacionRequest;
import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.01 Fase 01 - paso 1.2: el Analista de Cuentas por Pagar clasifica la
 * discrepancia (Precio o Cantidad). RN-AP1-05: al clasificar, el sistema
 * dispara automáticamente (paso 1.3) la notificación interna a Compras /
 * Category Manager.
 */
@Service
public class ClasificarExcepcionFacturacionUseCase {

    private final IExcepcionFacturacionRepository excepcionFacturacionRepository;

    public ClasificarExcepcionFacturacionUseCase(IExcepcionFacturacionRepository excepcionFacturacionRepository) {
        this.excepcionFacturacionRepository = excepcionFacturacionRepository;
    }

    @Transactional
    public ExcepcionFacturacionResponse clasificar(Long id, ClasificarExcepcionFacturacionRequest request) {
        ExcepcionFacturacion excepcion = excepcionFacturacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Excepción de facturación no encontrada: " + id));

        if (request.getTipoDiscrepancia() == null) {
            throw new BadRequestException("RN-AP1-04: el tipo de discrepancia es obligatorio (Precio o Cantidad)");
        }

        excepcion.clasificar(request.getTipoDiscrepancia());

        ExcepcionFacturacion guardada = excepcionFacturacionRepository.save(excepcion);
        return ExcepcionFacturacionResponseAssembler.toResponse(guardada);
    }
}
