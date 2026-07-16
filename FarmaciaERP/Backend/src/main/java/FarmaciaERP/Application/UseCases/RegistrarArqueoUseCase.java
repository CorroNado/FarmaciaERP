package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarArqueoRequest;
import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 01 - 1.2 Realizar Arqueo Físico vs. Valores Registrados en
 * Pantalla (RN-AR1-01).
 */
@Service
public class RegistrarArqueoUseCase {

    private final ICierreCajaRepository cierreCajaRepository;

    public RegistrarArqueoUseCase(ICierreCajaRepository cierreCajaRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Transactional
    public CierreCajaResponse ejecutar(Long cierreCajaId, RegistrarArqueoRequest request) {
        CierreCaja cierre = cierreCajaRepository.findById(cierreCajaId)
                .orElseThrow(() -> new BadRequestException("Cierre de caja no encontrado: " + cierreCajaId));

        cierre.registrarArqueo(request.getMontoContado());

        CierreCaja guardado = cierreCajaRepository.save(cierre);
        return CierreCajaResponseAssembler.toResponse(guardado);
    }
}
