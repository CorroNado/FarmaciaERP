package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.InterpretarArchivoTransferenciaRequest;
import FarmaciaERP.Application.DTOs.Response.CobroARResponse;
import FarmaciaERP.Domain.Entities.CobroAR;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Enums.EstadoRecetaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICobroARRepository;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 05 - 5.1 Interpretar Archivo de Transferencia bancaria
 * recibido de la entidad recaudadora (RN-AR5-01). Solo puede
 * interpretarse cuando la Fase 04 (Conciliación de Débitos y Ajustes
 * Técnicos) ya liberó el lote.
 */
@Service
public class InterpretarArchivoTransferenciaUseCase {

    private static final double RETENCIONES_POR_DEFECTO = 15.00;

    private final IContabilizacionARRepository contabilizacionARRepository;
    private final IRecetaMedicaARRepository recetaMedicaARRepository;
    private final ICobroARRepository cobroARRepository;
    private final BuscarDebitoARUseCase buscarDebitoARUseCase;

    public InterpretarArchivoTransferenciaUseCase(IContabilizacionARRepository contabilizacionARRepository,
                                                    IRecetaMedicaARRepository recetaMedicaARRepository,
                                                    ICobroARRepository cobroARRepository,
                                                    BuscarDebitoARUseCase buscarDebitoARUseCase) {
        this.contabilizacionARRepository = contabilizacionARRepository;
        this.recetaMedicaARRepository = recetaMedicaARRepository;
        this.cobroARRepository = cobroARRepository;
        this.buscarDebitoARUseCase = buscarDebitoARUseCase;
    }

    @Transactional
    public CobroARResponse ejecutar(InterpretarArchivoTransferenciaRequest request) {
        ContabilizacionAR contabilizacionAR = contabilizacionARRepository.findById(request.getContabilizacionARId())
                .orElseThrow(() -> new BadRequestException(
                        "Contabilización AR no encontrada: " + request.getContabilizacionARId()));

        if (!buscarDebitoARUseCase.puedeContinuarFase05(contabilizacionAR.getId())) {
            throw new BadRequestException(
                    "RN-AR4-01: la Fase 04 debe conciliar todos los débitos antes de habilitar la Fase 05");
        }
        if (cobroARRepository.findByContabilizacionARId(contabilizacionAR.getId()).isPresent()) {
            throw new BadRequestException("Ya existe un cobro interpretado para este lote");
        }

        double retenciones = request.getRetenciones() > 0 ? request.getRetenciones() : RETENCIONES_POR_DEFECTO;
        double montoEsperado = montoEsperadoTransferencia(contabilizacionAR);

        CobroAR cobro = CobroAR.interpretar(contabilizacionAR, montoEsperado, retenciones);
        CobroAR guardado = cobroARRepository.save(cobro);
        return CobroARResponseAssembler.toResponse(guardado);
    }

    /**
     * RN-AR5-01: el monto esperado de la transferencia es la cobertura
     * declarada por la aseguradora (Fase 01) cuando existe; en caso
     * contrario, es la suma de las recetas aprobadas/liberadas de la
     * Fase 03 más el copago del cliente.
     */
    private double montoEsperadoTransferencia(ContabilizacionAR contabilizacionAR) {
        double coberturaAseg = contabilizacionAR.getCierreCaja().getCoberturaAseg();
        if (coberturaAseg > 0) {
            return coberturaAseg;
        }

        double aprobadasLiberadas = recetaMedicaARRepository.findByContabilizacionARId(contabilizacionAR.getId())
                .stream()
                .filter(r -> r.getEstado() == EstadoRecetaAR.APROBADA || r.getEstado() == EstadoRecetaAR.LIBERADA)
                .mapToDouble(RecetaMedicaAR::getMontoDeclarado)
                .sum();

        return aprobadasLiberadas + contabilizacionAR.getCierreCaja().getCopago();
    }
}
