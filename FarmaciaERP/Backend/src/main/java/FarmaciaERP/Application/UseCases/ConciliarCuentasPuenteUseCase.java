package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.06 Fase 06 - paso 6.7: Conciliar Cuentas Puente Financieras y
 * Compensar Cuenta Transitoria del Banco (Gerente de Finanzas /
 * Tesorero). RN-AP6-09: concluye el ciclo FI-AP; se extingue la
 * obligación con el proveedor.
 */
@Service
public class ConciliarCuentasPuenteUseCase {

    private final IDispersionBancariaCierreRepository dispersionBancariaCierreRepository;

    public ConciliarCuentasPuenteUseCase(IDispersionBancariaCierreRepository dispersionBancariaCierreRepository) {
        this.dispersionBancariaCierreRepository = dispersionBancariaCierreRepository;
    }

    @Transactional
    public DispersionBancariaCierreResponse ejecutar(Long id) {
        DispersionBancariaCierre dispersion = dispersionBancariaCierreRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Dispersión bancaria de cierre no encontrada: " + id));

        dispersion.conciliarCuentasPuenteYCompensar();

        DispersionBancariaCierre guardada = dispersionBancariaCierreRepository.save(dispersion);
        return DispersionBancariaCierreResponseAssembler.toResponse(guardada);
    }
}
