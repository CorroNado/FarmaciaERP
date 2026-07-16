package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IniciarContabilizacionARRequest;
import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 02 - Inicia la Contabilización y Declaración de Valores a
 * partir de un cierre de caja de la Fase 01 ya clasificado (RN-AR2-01).
 */
@Service
public class IniciarContabilizacionARUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;
    private final ICierreCajaRepository cierreCajaRepository;

    public IniciarContabilizacionARUseCase(IContabilizacionARRepository contabilizacionARRepository,
                                            ICierreCajaRepository cierreCajaRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
        this.cierreCajaRepository = cierreCajaRepository;
    }

    @Transactional
    public ContabilizacionARResponse ejecutar(IniciarContabilizacionARRequest request) {
        if (contabilizacionARRepository.findByCierreCajaId(request.getCierreCajaId()).isPresent()) {
            throw new BadRequestException("Ya existe una contabilización de Fase 02 para este cierre de caja");
        }

        CierreCaja cierreCaja = cierreCajaRepository.findById(request.getCierreCajaId())
                .orElseThrow(() -> new BadRequestException("Cierre de caja no encontrado: " + request.getCierreCajaId()));

        ContabilizacionAR contabilizacion = ContabilizacionAR.iniciar(cierreCaja);
        ContabilizacionAR guardada = contabilizacionARRepository.save(contabilizacion);
        return ContabilizacionARResponseAssembler.toResponse(guardada);
    }
}
