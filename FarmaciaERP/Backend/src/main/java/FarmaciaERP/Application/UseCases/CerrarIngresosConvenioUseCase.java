package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 06 - Cerrar Ingresos por Convenio (RN-AR6-01), paso
 * terminal que finaliza exitosamente el ciclo FI-AR del período.
 */
@Service
public class CerrarIngresosConvenioUseCase {

    private final ICompensacionARRepository compensacionARRepository;

    public CerrarIngresosConvenioUseCase(ICompensacionARRepository compensacionARRepository) {
        this.compensacionARRepository = compensacionARRepository;
    }

    @Transactional
    public CompensacionARResponse ejecutar(Long id) {
        CompensacionAR compensacion = compensacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Compensación final AR no encontrada: " + id));

        compensacion.cerrarIngresosPorConvenio();

        CompensacionAR guardada = compensacionARRepository.save(compensacion);
        return CompensacionARResponseAssembler.toResponse(guardada);
    }
}
