package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CompensacionARResponse;
import FarmaciaERP.Domain.Entities.CompensacionAR;
import FarmaciaERP.Domain.Entities.ContabilizacionAR;
import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Domain.Enums.EstadoRecetaAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICompensacionARRepository;
import FarmaciaERP.Domain.Repositories.IRecetaMedicaARRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 06 - Generar Reporte de Rendimiento Comercial y Margen
 * de la Cadena (RN-AR6-01). El margen neto del período resulta de las
 * ventas del día (POS) menos las pérdidas por débitos no subsanables
 * (ajustes técnicos contables aplicados en la Fase 04).
 */
@Service
public class GenerarReporteRendimientoUseCase {

    private final ICompensacionARRepository compensacionARRepository;
    private final IRecetaMedicaARRepository recetaMedicaARRepository;
    private final BuscarDebitoARUseCase buscarDebitoARUseCase;

    public GenerarReporteRendimientoUseCase(ICompensacionARRepository compensacionARRepository,
                                             IRecetaMedicaARRepository recetaMedicaARRepository,
                                             BuscarDebitoARUseCase buscarDebitoARUseCase) {
        this.compensacionARRepository = compensacionARRepository;
        this.recetaMedicaARRepository = recetaMedicaARRepository;
        this.buscarDebitoARUseCase = buscarDebitoARUseCase;
    }

    @Transactional
    public CompensacionARResponse ejecutar(Long id) {
        CompensacionAR compensacion = compensacionARRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Compensación final AR no encontrada: " + id));

        ContabilizacionAR contabilizacionAR = compensacion.getContabilizacionAR();
        double montoVentas = contabilizacionAR.getCierreCaja().getReporteVentas();
        double montoAprobadas = recetaMedicaARRepository.findByContabilizacionARId(contabilizacionAR.getId())
                .stream()
                .filter(r -> r.getEstado() == EstadoRecetaAR.APROBADA || r.getEstado() == EstadoRecetaAR.LIBERADA)
                .mapToDouble(RecetaMedicaAR::getMontoDeclarado)
                .sum();
        double perdidas = buscarDebitoARUseCase.ajusteTotal(contabilizacionAR.getId());

        compensacion.generarReporteRendimiento(montoVentas, montoAprobadas, perdidas);

        CompensacionAR guardada = compensacionARRepository.save(compensacion);
        return CompensacionARResponseAssembler.toResponse(guardada);
    }
}
