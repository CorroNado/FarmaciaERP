package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarRespuestaAseguradoraRequest;
import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 03 - Registro de la Respuesta de la Aseguradora a la
 * impugnación enviada (RN-AR3-01).
 */
@Service
public class RegistrarRespuestaAseguradoraUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;

    public RegistrarRespuestaAseguradoraUseCase(IRecetaMedicaARRepository recetaMedicaARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
    }

    @Transactional
    public RecetaMedicaARResponse ejecutar(Long id, RegistrarRespuestaAseguradoraRequest request) {
        RecetaMedicaAR receta = recetaMedicaARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Receta médica AR no encontrada: " + id));

        receta.registrarRespuestaAseguradora(request.isAceptaImpugnacion());

        RecetaMedicaAR guardada = recetaMedicaARRepository.save(receta);
        return RecetaMedicaARResponseAssembler.toResponse(guardada);
    }
}
