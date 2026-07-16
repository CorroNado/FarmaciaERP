package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.06 Fase 06 - paso 6.1: Compilar Propuesta de Pago (F110) recibida
 * desde la Fase 05 (Sistema ERP).
 */
@Service
public class CompilarPropuestaPagoUseCase {

    private final IDispersionBancariaCierreRepository dispersionBancariaCierreRepository;

    public CompilarPropuestaPagoUseCase(IDispersionBancariaCierreRepository dispersionBancariaCierreRepository) {
        this.dispersionBancariaCierreRepository = dispersionBancariaCierreRepository;
    }

    @Transactional
    public DispersionBancariaCierreResponse ejecutar(Long id) {
        DispersionBancariaCierre dispersion = dispersionBancariaCierreRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Dispersión bancaria de cierre no encontrada: " + id));

        dispersion.compilarPropuestaPago();

        DispersionBancariaCierre guardada = dispersionBancariaCierreRepository.save(dispersion);
        return DispersionBancariaCierreResponseAssembler.toResponse(guardada);
    }
}
