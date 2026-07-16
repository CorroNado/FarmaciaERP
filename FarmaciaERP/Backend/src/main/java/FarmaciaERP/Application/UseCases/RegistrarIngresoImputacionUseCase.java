package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 05 - 6.1 Registrar Ingreso de Dinero e Imputación en la
 * cuenta del cliente (Sistema ERP), cierre del ciclo de Cobros e
 * Imputación Bancaria que habilita la Fase 06 (RN-AR5-01).
 */
@Service
public class RegistrarIngresoImputacionUseCase {

    private final ICobroARRepository cobroARRepository;

    public RegistrarIngresoImputacionUseCase(ICobroARRepository cobroARRepository) {
        this.cobroARRepository = cobroARRepository;
    }

    @Transactional
    public CobroARResponse ejecutar(Long id) {
        CobroAR cobro = cobroARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cobro AR no encontrado: " + id));

        cobro.registrarIngresoImputacion();

        CobroAR guardado = cobroARRepository.save(cobro);
        return CobroARResponseAssembler.toResponse(guardado);
    }
}
