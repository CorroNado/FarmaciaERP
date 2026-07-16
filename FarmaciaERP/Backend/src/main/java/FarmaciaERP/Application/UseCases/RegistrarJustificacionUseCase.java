package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarJustificacionRequest;
import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 01 - 2.1 Registrar Justificación de Faltante o Sobrante en
 * Sistema (RN-AR1-01).
 */
@Service
public class RegistrarJustificacionUseCase {

    private final ICierreCajaRepository cierreCajaRepository;

    public RegistrarJustificacionUseCase(ICierreCajaRepository cierreCajaRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Transactional
    public CierreCajaResponse ejecutar(Long cierreCajaId, RegistrarJustificacionRequest request) {
        CierreCaja cierre = cierreCajaRepository.findById(cierreCajaId)
                .orElseThrow(() -> new BadRequestException("Cierre de caja no encontrado: " + cierreCajaId));

        cierre.registrarJustificacion(request.getJustificacion());

        CierreCaja guardado = cierreCajaRepository.save(cierre);
        return CierreCajaResponseAssembler.toResponse(guardado);
    }
}
