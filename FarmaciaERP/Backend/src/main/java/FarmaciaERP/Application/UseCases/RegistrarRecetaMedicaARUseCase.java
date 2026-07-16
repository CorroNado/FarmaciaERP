package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarRecetaMedicaARRequest;
import FarmaciaERP.Application.DTOs.Response.RecetaMedicaARResponse;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 03 - Recepción física de la receta médica dentro del lote
 * consolidado de la Fase 02, en espera de auditoría (RN-AR3-01).
 */
@Service
public class RegistrarRecetaMedicaARUseCase {

    private final IRecetaMedicaARRepository recetaMedicaARRepository;
    private final IContabilizacionARRepository contabilizacionARRepository;

    public RegistrarRecetaMedicaARUseCase(IRecetaMedicaARRepository recetaMedicaARRepository,
                                           IContabilizacionARRepository contabilizacionARRepository) {
        this.recetaMedicaARRepository = recetaMedicaARRepository;
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    @Transactional
    public RecetaMedicaARResponse ejecutar(RegistrarRecetaMedicaARRequest request) {
        ContabilizacionAR contabilizacionAR = contabilizacionARRepository.findById(request.getContabilizacionARId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + request.getContabilizacionARId()));

        RecetaMedicaAR receta = RecetaMedicaAR.registrar(
                request.getNumero(),
                contabilizacionAR,
                request.getMedicamento(),
                request.getAseguradora(),
                request.getMontoDeclarado(),
                request.getMontoPreliquidado());

        RecetaMedicaAR guardada = recetaMedicaARRepository.save(receta);
        return RecetaMedicaARResponseAssembler.toResponse(guardada);
    }
}
