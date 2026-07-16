package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.ValidarTroquelesFirmasRequest;
import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 03 - Validación de Troqueles, Firmas y Vigencia de la
 * receta física. RN-AR3-01: el rechazo se registra en SAP (ZFMR_RECHAZO).
 */
@Service
public class ValidarTroquelesFirmasUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;

    public ValidarTroquelesFirmasUseCase(IRecetaMedicaARRepository recetaMedicaARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
    }

    @Transactional
    public RecetaMedicaARResponse ejecutar(Long id, ValidarTroquelesFirmasRequest request) {
        RecetaMedicaAR receta = recetaMedicaARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Receta médica AR no encontrada: " + id));

        receta.validarTroquelesFirmas(request.isValido(), request.getMotivoRechazo());

        RecetaMedicaAR guardada = recetaMedicaARRepository.save(receta);
        return RecetaMedicaARResponseAssembler.toResponse(guardada);
    }
}
