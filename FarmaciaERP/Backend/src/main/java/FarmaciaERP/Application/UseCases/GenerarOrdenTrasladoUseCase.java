package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.GenerarOrdenTrasladoRequest;
import FarmaciaERP.Application.DTOs.Response.OrdenTrasladoResponse;
import FarmaciaERP.Domain.Entities.InspeccionCalidad;
import FarmaciaERP.Domain.Entities.OrdenTraslado;
import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IInspeccionCalidadRepository;
import FarmaciaERP.Domain.Repositories.IOrdenTrasladoRepository;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.07 Fase 06 - RN-E6-002 · RN-E6-004 · RN-E6-005 · RN-E6-007 ·
 * RN-E6-008: dispara el algoritmo Push/Pull sobre un lote en Libre
 * Utilización (Decisión de Empleo aprobada en QA11), genera la Orden de
 * Traslado (STO) con su guía de remisión y la despacha hacia la sucursal
 * destino. El stock queda "en tránsito" hasta la confirmación de
 * recepción en el POS (RN-E6-009, ver ConfirmarRecepcionUseCase).
 */
@Service
public class GenerarOrdenTrasladoUseCase {

    private final IOrdenTrasladoRepository ordenTrasladoRepository;
    private final IInspeccionCalidadRepository inspeccionCalidadRepository;
    private final ISucursalRepository sucursalRepository;

    public GenerarOrdenTrasladoUseCase(IOrdenTrasladoRepository ordenTrasladoRepository,
                                        IInspeccionCalidadRepository inspeccionCalidadRepository,
                                        ISucursalRepository sucursalRepository) {
        this.ordenTrasladoRepository = ordenTrasladoRepository;
        this.inspeccionCalidadRepository = inspeccionCalidadRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public OrdenTrasladoResponse ejecutar(GenerarOrdenTrasladoRequest request) {
        InspeccionCalidad inspeccionCalidad = inspeccionCalidadRepository
                .findById(request.getInspeccionCalidadId())
                .orElseThrow(() -> new BadRequestException(
                        "Decisión de Empleo (QA11) no encontrada: " + request.getInspeccionCalidadId()));

        // RN-E6-002: el lote ya debe contar con una STO previa evita doble despacho del mismo lote.
        if (ordenTrasladoRepository.existsByInspeccionCalidadId(inspeccionCalidad.getId())) {
            throw new BadRequestException(
                    "El lote " + inspeccionCalidad.getEntradaMercancia().getLote()
                            + " ya cuenta con una Orden de Traslado generada");
        }

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalDestinoId())
                .orElseThrow(() -> new BadRequestException(
                        "Sucursal destino no encontrada: " + request.getSucursalDestinoId()));

        String guia = (request.getGuiaRemision() == null || request.getGuiaRemision().isBlank())
                ? generarGuiaRemision()
                : request.getGuiaRemision().trim();

        OrdenTraslado orden = OrdenTraslado.generar(generarNumeroSTO(), inspeccionCalidad, sucursal, guia);
        OrdenTraslado guardada = ordenTrasladoRepository.save(orden);
        return OrdenTrasladoResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroSTO() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "STO-" + Year.now().getValue() + "-" + correlativo;
    }

    private String generarGuiaRemision() {
        int correlativo = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "GR-" + Year.now().getValue() + "-" + correlativo;
    }
}
