package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RechazarLoteRequest;
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
 * LOG.06 Fase 05 - RN-E5-007: rechaza el lote cuando algún control de
 * calidad no es conforme. El estado queda bloqueado automáticamente y se
 * inicia la devolución al proveedor.
 */
@Service
public class RechazarLoteUseCase {

    private final IInspeccionCalidadRepository inspeccionCalidadRepository;
    private final IEntradaMercanciaRepository entradaMercanciaRepository;

    public RechazarLoteUseCase(IInspeccionCalidadRepository inspeccionCalidadRepository,
                                IEntradaMercanciaRepository entradaMercanciaRepository) {
        this.inspeccionCalidadRepository = inspeccionCalidadRepository;
        this.entradaMercanciaRepository = entradaMercanciaRepository;
    }

    @Transactional
    public InspeccionCalidadResponse ejecutar(RechazarLoteRequest request) {
        EntradaMercancia entradaMercancia = entradaMercanciaRepository.findById(request.getEntradaMercanciaId())
                .orElseThrow(() -> new BadRequestException(
                        "Entrada de mercancía (MIGO) no encontrada: " + request.getEntradaMercanciaId()));

        if (inspeccionCalidadRepository.existsByEntradaMercanciaId(entradaMercancia.getId())) {
            throw new BadRequestException(
                    "El lote de la entrada " + entradaMercancia.getNumero() + " ya cuenta con una Decisión de Empleo registrada");
        }

        String numero = generarNumeroQA();
        InspeccionCalidad inspeccion = InspeccionCalidad.rechazar(
                numero,
                entradaMercancia,
                request.getMotivoRechazo(),
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
