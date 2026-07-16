package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.AprobarFuenteRequest;
import FarmaciaERP.Application.DTOs.Response.SolPedResponse;
import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConvenioRepository;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import FarmaciaERP.Domain.Repositories.ISolicitudPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LOG.02 Fase 02 - Determinación de la fuente de aprovisionamiento.
 * RN-MM-001: convenio marco vigente. RN-MM-004: Info-Record congela precios.
 * RN-MM-005: control presupuestal contra el Centro de Costo.
 */
@Service
public class AprobarFuenteAprovisionamientoUseCase {

    private final ISolicitudPedidoRepository solicitudPedidoRepository;
    private final IProveedorRepository proveedorRepository;
    private final IConvenioRepository convenioRepository;

    public AprobarFuenteAprovisionamientoUseCase(ISolicitudPedidoRepository solicitudPedidoRepository,
                                                  IProveedorRepository proveedorRepository,
                                                  IConvenioRepository convenioRepository) {
        this.solicitudPedidoRepository = solicitudPedidoRepository;
        this.proveedorRepository = proveedorRepository;
        this.convenioRepository = convenioRepository;
    }

    @Transactional
    public SolPedResponse ejecutar(Long solPedId, AprobarFuenteRequest request) {
        SolicitudPedido solPed = solicitudPedidoRepository.findById(solPedId)
                .orElseThrow(() -> new BadRequestException("SolPed no encontrada: " + solPedId));

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new BadRequestException("Proveedor no encontrado: " + request.getProveedorId()));

        Convenio convenio = convenioRepository.findById(request.getConvenioId())
                .orElseThrow(() -> new BadRequestException("Convenio no encontrado: " + request.getConvenioId()));

        if (!convenio.getProveedor().getId().equals(proveedor.getId())) {
            throw new BadRequestException("RN-MM-001: el convenio seleccionado no pertenece al proveedor indicado");
        }

        solPed.aprobarFuente(proveedor, convenio);

        SolicitudPedido actualizada = solicitudPedidoRepository.save(solPed);
        return SolPedResponseAssembler.toResponse(actualizada);
    }
}
