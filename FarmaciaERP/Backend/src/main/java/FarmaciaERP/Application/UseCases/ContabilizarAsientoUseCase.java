package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContabilizarAsientoUseCase {

    private final IAsientoContableRepository asientoRepository;

    public ContabilizarAsientoUseCase(IAsientoContableRepository asientoRepository) {
        this.asientoRepository = asientoRepository;
    }

    @Transactional
    public AsientoContableResponse ejecutar(Long id) {
        AsientoContable asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Asiento contable no encontrado: " + id));

        asiento.contabilizar();
        AsientoContable actualizado = asientoRepository.save(asiento);
        return AsientoContableResponseAssembler.toResponse(actualizado);
    }
}