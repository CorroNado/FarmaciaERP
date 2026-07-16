package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AplicarCompensacionAutomaticaRequest;
import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 06 - 6.1 Aplicar Compensación Automática sobre la
 * Cuenta del Cliente (Tesorería / Finanzas), apertura del ciclo de
 * Compensación Final y Análisis de Margen Neto (RN-AR6-01). Solo
 * puede aplicarse cuando la Fase 05 ya registró el ingreso de dinero
 * e imputación bancaria.
 */
@Service
public class AplicarCompensacionAutomaticaUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;
    private final ICompensacionARRepository compensacionARRepository;
    private final BuscarCobroARUseCase buscarCobroARUseCase;

    public AplicarCompensacionAutomaticaUseCase(IContabilizacionARRepository contabilizacionARRepository,
                                                 ICompensacionARRepository compensacionARRepository,
                                                 BuscarCobroARUseCase buscarCobroARUseCase) {
        this.contabilizacionARRepository = contabilizacionARRepository;
        this.compensacionARRepository = compensacionARRepository;
        this.buscarCobroARUseCase = buscarCobroARUseCase;
    }

    @Transactional
    public CompensacionARResponse ejecutar(AplicarCompensacionAutomaticaRequest request) {
        ContabilizacionAR contabilizacionAR = contabilizacionARRepository.findById(request.getContabilizacionARId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + request.getContabilizacionARId()));

        if (!buscarCobroARUseCase.puedeContinuarFase06(contabilizacionAR.getId())) {
            throw new BadRequestException(
                    "RN-AR5-01: la Fase 05 debe registrar el ingreso e imputación bancaria antes de habilitar la Fase 06");
        }
        if (compensacionARRepository.findByContabilizacionARId(contabilizacionAR.getId()).isPresent()) {
            throw new BadRequestException("Ya existe una compensación final iniciada para este lote");
        }

        CompensacionAR compensacion = CompensacionAR.aplicarCompensacionAutomatica(contabilizacionAR);
        CompensacionAR guardada = compensacionARRepository.save(compensacion);
        return CompensacionARResponseAssembler.toResponse(guardada);
    }
}
