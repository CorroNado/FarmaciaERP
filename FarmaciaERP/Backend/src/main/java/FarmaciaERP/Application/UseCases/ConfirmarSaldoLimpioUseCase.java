package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 06 - Confirmar Saldo Limpio en Cuentas Corrientes
 * (RN-AR6-01), previo al cierre de ingresos por convenio.
 */
@Service
public class ConfirmarSaldoLimpioUseCase {

    private final ICompensacionARRepository compensacionARRepository;

    public ConfirmarSaldoLimpioUseCase(ICompensacionARRepository compensacionARRepository) {
        this.compensacionARRepository = compensacionARRepository;
    }

    @Transactional
    public CompensacionARResponse ejecutar(Long id) {
        CompensacionAR compensacion = compensacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Compensación final AR no encontrada: " + id));

        compensacion.confirmarSaldoLimpio();

        CompensacionAR guardada = compensacionARRepository.save(compensacion);
        return CompensacionARResponseAssembler.toResponse(guardada);
    }
}
