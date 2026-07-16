package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 05 - 5.3 Ingresar Ajuste Contable por Diferencia
 * (RN-AR5-01), aplicado por el Analista AR cuando la conciliación de
 * comisiones y retenciones detecta un descuadre en los montos.
 */
@Service
public class IngresarAjusteContableCobroUseCase {

    private final ICobroARRepository cobroARRepository;

    public IngresarAjusteContableCobroUseCase(ICobroARRepository cobroARRepository) {
        this.cobroARRepository = cobroARRepository;
    }

    @Transactional
    public CobroARResponse ejecutar(Long id) {
        CobroAR cobro = cobroARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cobro AR no encontrado: " + id));

        cobro.ingresarAjusteContablePorDiferencia();

        CobroAR guardado = cobroARRepository.save(cobro);
        return CobroARResponseAssembler.toResponse(guardado);
    }
}
