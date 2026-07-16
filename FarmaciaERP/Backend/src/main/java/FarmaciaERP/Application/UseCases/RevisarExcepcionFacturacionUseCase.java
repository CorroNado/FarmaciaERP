package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.01 Fase 01 - paso 1.1: el Analista de Cuentas por Pagar revisa el
 * panel de facturas bloqueadas.
 */
@Service
public class RevisarExcepcionFacturacionUseCase {

    private final IExcepcionFacturacionRepository excepcionFacturacionRepository;

    public RevisarExcepcionFacturacionUseCase(IExcepcionFacturacionRepository excepcionFacturacionRepository) {
        this.excepcionFacturacionRepository = excepcionFacturacionRepository;
    }

    @Transactional
    public ExcepcionFacturacionResponse revisar(Long id) {
        ExcepcionFacturacion excepcion = excepcionFacturacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Excepción de facturación no encontrada: " + id));

        excepcion.revisar();

        ExcepcionFacturacion guardada = excepcionFacturacionRepository.save(excepcion);
        return ExcepcionFacturacionResponseAssembler.toResponse(guardada);
    }
}
