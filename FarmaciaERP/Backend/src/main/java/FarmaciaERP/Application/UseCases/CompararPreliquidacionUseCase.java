package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CompararPreliquidacionRequest;
import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 03 - Comparación contra la Pre-liquidación de la
 * Aseguradora. RN-AR3-01: toda inconsistencia genera una impugnación
 * formal (ZFMR_IMPUGNACION).
 */
@Service
public class CompararPreliquidacionUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;

    public CompararPreliquidacionUseCase(IRecetaMedicaARRepository recetaMedicaARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
    }

    @Transactional
    public RecetaMedicaARResponse ejecutar(Long id, CompararPreliquidacionRequest request) {
        RecetaMedicaAR receta = recetaMedicaARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Receta médica AR no encontrada: " + id));

        receta.compararPreliquidacion(request.isCoincide(), request.getInconsistencia());

        RecetaMedicaAR guardada = recetaMedicaARRepository.save(receta);
        return RecetaMedicaARResponseAssembler.toResponse(guardada);
    }
}
