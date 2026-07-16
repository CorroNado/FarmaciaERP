package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CapturarExcepcionFacturacionRequest;
import FarmaciaERP.Application.DTOs.Response.ExcepcionFacturacionResponse;
import FarmaciaERP.Domain.Entities.ConciliacionTresVias;
import FarmaciaERP.Domain.Entities.ExcepcionFacturacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConciliacionTresViasRepository;
import FarmaciaERP.Domain.Repositories.IExcepcionFacturacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.01 Fase 01 - Captura de Excepciones de Facturación (Frontera
 * Logística). RN-AP1-01: el sistema bloquea automáticamente toda factura de
 * proveedor cuya conciliación de 3 vías (MRBR) haya resultado bloqueada por
 * desviación de precio o cantidad.
 */
@Service
public class CapturarExcepcionFacturacionUseCase {

    private final IExcepcionFacturacionRepository excepcionFacturacionRepository;
    private final IConciliacionTresViasRepository conciliacionTresViasRepository;

    public CapturarExcepcionFacturacionUseCase(IExcepcionFacturacionRepository excepcionFacturacionRepository,
                                                 IConciliacionTresViasRepository conciliacionTresViasRepository) {
        this.excepcionFacturacionRepository = excepcionFacturacionRepository;
        this.conciliacionTresViasRepository = conciliacionTresViasRepository;
    }

    @Transactional
    public ExcepcionFacturacionResponse capturar(CapturarExcepcionFacturacionRequest request) {
        if (request.getConciliacionTresViasId() == null) {
            throw new BadRequestException("El identificador de la conciliación de 3 vías (MRBR) es obligatorio");
        }

        ConciliacionTresVias conciliacion = conciliacionTresViasRepository
                .findById(request.getConciliacionTresViasId())
                .orElseThrow(() -> new BadRequestException(
                        "Conciliación de 3 vías no encontrada: " + request.getConciliacionTresViasId()));

        // RN-AP1-01: no debe existir ya una excepción capturada para la misma conciliación.
        if (excepcionFacturacionRepository.existsByConciliacionTresViasId(conciliacion.getId())) {
            throw new BadRequestException(
                    "Ya existe una excepción de facturación capturada para la conciliación " + conciliacion.getNumero());
        }

        String numero = generarNumeroExcepcion();
        ExcepcionFacturacion excepcion = ExcepcionFacturacion.capturar(numero, conciliacion);

        ExcepcionFacturacion guardada = excepcionFacturacionRepository.save(excepcion);
        return ExcepcionFacturacionResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroExcepcion() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "EXC-AP-" + Year.now().getValue() + "-" + correlativo;
    }
}
