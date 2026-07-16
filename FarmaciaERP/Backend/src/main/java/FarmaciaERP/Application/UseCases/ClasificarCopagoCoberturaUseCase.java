package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 01 - 1.3 Clasificar de forma Automática Copagos y Coberturas
 * de Aseguradoras (RN-AR1-01). Al completarse, el ciclo queda habilitado
 * para continuar a la Fase 02 — Contabilización y Declaración de Valores.
 */
@Service
public class ClasificarCopagoCoberturaUseCase {

    private final ICierreCajaRepository cierreCajaRepository;

    public ClasificarCopagoCoberturaUseCase(ICierreCajaRepository cierreCajaRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Transactional
    public CierreCajaResponse ejecutar(Long cierreCajaId) {
        CierreCaja cierre = cierreCajaRepository.findById(cierreCajaId)
                .orElseThrow(() -> new BadRequestException("Cierre de caja no encontrado: " + cierreCajaId));

        cierre.clasificarCopagoCobertura();

        CierreCaja guardado = cierreCajaRepository.save(cierre);
        return CierreCajaResponseAssembler.toResponse(guardado);
    }
}
