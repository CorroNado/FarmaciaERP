package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AprobarLoteRequest;
import FarmaciaERP.Application.DTOs.Response.InspeccionCalidadResponse;
import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Domain.Entities.InspeccionCalidad;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Domain.Repositories.IInspeccionCalidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.06 Fase 05 - RN-E5-001 · RN-E5-002 · RN-E5-003 · RN-E5-004 · RN-E5-006 ·
 * RN-E5-010: aprueba el lote (Decisión de Empleo) únicamente si todos los
 * controles de calidad son conformes, incluida la cadena de frío heredada
 * de la entrada de mercancía (MIGO). El lote aprobado queda en Libre
 * Utilización, habilitado para la Fase 06 (stock y distribución).
 */
@Service
public class AprobarLoteUseCase {

    private final IInspeccionCalidadRepository inspeccionCalidadRepository;
    private final IEntradaMercanciaRepository entradaMercanciaRepository;

    public AprobarLoteUseCase(IInspeccionCalidadRepository inspeccionCalidadRepository,
                               IEntradaMercanciaRepository entradaMercanciaRepository) {
        this.inspeccionCalidadRepository = inspeccionCalidadRepository;
        this.entradaMercanciaRepository = entradaMercanciaRepository;
    }

    @Transactional
    public InspeccionCalidadResponse ejecutar(AprobarLoteRequest request) {
        EntradaMercancia entradaMercancia = entradaMercanciaRepository.findById(request.getEntradaMercanciaId())
                .orElseThrow(() -> new BadRequestException(
                        "Entrada de mercancía (MIGO) no encontrada: " + request.getEntradaMercanciaId()));

        if (inspeccionCalidadRepository.existsByEntradaMercanciaId(entradaMercancia.getId())) {
            throw new BadRequestException(
                    "El lote de la entrada " + entradaMercancia.getNumero() + " ya cuenta con una Decisión de Empleo registrada");
        }

        String numero = generarNumeroQA();
        InspeccionCalidad inspeccion = InspeccionCalidad.aprobar(
                numero,
                entradaMercancia,
                request.isMuestreoConforme(),
                request.isRegistroSanitarioVigente(),
                request.isEmpaqueConforme()
        );

        InspeccionCalidad guardada = inspeccionCalidadRepository.save(inspeccion);
        return InspeccionCalidadResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroQA() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "QA11-" + Year.now().getValue() + "-" + correlativo;
    }
}
