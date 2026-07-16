package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DispersionBancariaCierreResponse;
import FarmaciaERP.Domain.Entities.DispersionBancariaCierre;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IDispersionBancariaCierreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AP.06 Fase 06 - paso 6.5: Ejecutar Transferencias en Banca Empresa
 * (Token) (Analista de Cuentas por Pagar).
 */
@Service
public class EjecutarTransferenciasBancariasUseCase {

    private final IDispersionBancariaCierreRepository dispersionBancariaCierreRepository;

    public EjecutarTransferenciasBancariasUseCase(IDispersionBancariaCierreRepository dispersionBancariaCierreRepository) {
        this.dispersionBancariaCierreRepository = dispersionBancariaCierreRepository;
    }

    @Transactional
    public DispersionBancariaCierreResponse ejecutar(Long id) {
        DispersionBancariaCierre dispersion = dispersionBancariaCierreRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Dispersión bancaria de cierre no encontrada: " + id));

        dispersion.ejecutarTransferenciasBancarias();

        DispersionBancariaCierre guardada = dispersionBancariaCierreRepository.save(dispersion);
        return DispersionBancariaCierreResponseAssembler.toResponse(guardada);
    }
}
