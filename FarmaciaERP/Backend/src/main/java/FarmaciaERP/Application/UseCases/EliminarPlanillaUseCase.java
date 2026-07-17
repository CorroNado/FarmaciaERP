package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IPlanillaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RRHH.03 Fase de Nómina - Eliminar Planilla Guardada: espejo de
 * eliminarPlanillaGuardada() del prototipo.
 */
@Service
public class EliminarPlanillaUseCase {

    private final IPlanillaRepository planillaRepository;

    public EliminarPlanillaUseCase(IPlanillaRepository planillaRepository) {
        this.planillaRepository = planillaRepository;
    }

    @Transactional
    public void ejecutar(Long id) {
        if (!planillaRepository.existsById(id)) {
            throw new BadRequestException("Planilla no encontrada: " + id);
        }
        planillaRepository.deleteById(id);
    }
}
