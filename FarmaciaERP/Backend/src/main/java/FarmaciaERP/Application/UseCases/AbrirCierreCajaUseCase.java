package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AbrirCierreCajaRequest;
import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FI-AR · Fase 01 - 1.1 Emitir Reporte Consolidado de Ventas del Mostrador:
 * abre el ciclo de Recepción y Auditoría del Cierre de Venta (RN-AR1-01).
 */
@Service
public class AbrirCierreCajaUseCase {

    private final ICierreCajaRepository cierreCajaRepository;
    private final ISucursalRepository sucursalRepository;

    public AbrirCierreCajaUseCase(ICierreCajaRepository cierreCajaRepository,
                                   ISucursalRepository sucursalRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public CierreCajaResponse ejecutar(AbrirCierreCajaRequest request) {
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new BadRequestException("Sucursal no encontrada: " + request.getSucursalId()));

        CierreCaja cierre = CierreCaja.abrir(request.getNumero(), sucursal, request.getReporteVentas());
        CierreCaja guardado = cierreCajaRepository.save(cierre);
        return CierreCajaResponseAssembler.toResponse(guardado);
    }
}
