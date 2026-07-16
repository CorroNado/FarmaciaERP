package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CalcularDebitoARRequest;
import FarmaciaERP.Application.DTOs.Response.DebitoARResponse;
import FarmaciaERP.Domain.Entities.DebitoAR;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDebitoARRepository;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 04 - Cálculo del débito a partir de una receta médica
 * (Fase 03) rechazada o con débito confirmado por la aseguradora
 * (RN-AR4-01).
 */
@Service
public class CalcularDebitoARUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;
    private final IDebitoARRepository debitoARRepository;

    public CalcularDebitoARUseCase(IRecetaMedicaARRepository recetaMedicaARRepository,
                                    IDebitoARRepository debitoARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
        this.debitoARRepository = debitoARRepository;
    }

    @Transactional
    public DebitoARResponse ejecutar(CalcularDebitoARRequest request) {
        RecetaMedicaAR receta = recetaMedicaARRepository.findById(request.getRecetaMedicaARId())
                .orElseThrow(() -> new BadRequestException(
                        "Receta médica AR no encontrada: " + request.getRecetaMedicaARId()));

        if (debitoARRepository.findByRecetaMedicaARId(receta.getId()).isPresent()) {
            throw new BadRequestException("Ya existe un débito calculado para la receta: " + receta.getNumero());
        }

        DebitoAR debito = DebitoAR.calcular(receta);
        DebitoAR guardado = debitoARRepository.save(debito);
        return DebitoARResponseAssembler.toResponse(guardado);
    }
}
