package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 01 - Rama de variación: el Auditor Médico Corporativo envía
 * los Físicos de Recetas Médicas a Oficina Central para su verificación
 * posterior (RN-AR1-01).
 */
@Service
public class EnviarFisicosRecetasUseCase {

    private final ICierreCajaRepository cierreCajaRepository;

    public EnviarFisicosRecetasUseCase(ICierreCajaRepository cierreCajaRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Transactional
    public CierreCajaResponse ejecutar(Long cierreCajaId) {
        CierreCaja cierre = cierreCajaRepository.findById(cierreCajaId)
                .orElseThrow(() -> new BadRequestException("Cierre de caja no encontrado: " + cierreCajaId));

        cierre.enviarFisicosRecetas();

        CierreCaja guardado = cierreCajaRepository.save(cierre);
        return CierreCajaResponseAssembler.toResponse(guardado);
    }
}
