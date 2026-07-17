package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarFacturaMIRORequest;
import FarmaciaERP.Application.DTOs.Response.FacturaMIROResponse;
import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.07 Fase 07 - VerificaciÃƒÂ³n de factura (MIRO). RN: MM-T12: se recibe la
 * factura electrÃƒÂ³nica del proveedor y se registra en el sistema asociÃƒÂ¡ndola
 * a la Orden de Compra, validando que esta cuente con entrada de mercancÃƒÂ­a
 * (MIGO) registrada y que no exista otra factura previamente asociada.
 */
@Service
@RequiredArgsConstructor
public class RegistrarFacturaMIROUseCase {

    private final IFacturaMIRORepository facturaMIRORepository;
    private final IOrdenCompraRepository ordenCompraRepository;
    private final IEntradaMercanciaRepository entradaMercanciaRepository;
    private final GenerarAsientoCompraUseCase generarAsientoCompraUseCase;

    @Transactional
    public FacturaMIROResponse ejecutar(RegistrarFacturaMIRORequest request) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(request.getOrdenCompraId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada: " + request.getOrdenCompraId()));

        // RN-F07-004: la OC debe contar con entrada de mercancÃƒÂ­a (MIGO) registrada
        // antes de poder facturarse.
        if (!entradaMercanciaRepository.existsByOrdenCompraId(ordenCompra.getId())) {
            throw new BadRequestException(
                    "RN-F07-004: la Orden de Compra " + ordenCompra.getNumero()
                            + " no tiene una entrada de mercancÃƒÂ­a (MIGO) registrada");
        }

        if (facturaMIRORepository.existsByOrdenCompraId(ordenCompra.getId())) {
            throw new BadRequestException(
                    "Ya existe una factura (MIRO) registrada para la Orden de Compra " + ordenCompra.getNumero());
        }

        if (facturaMIRORepository.findByNumeroFactura(request.getNumeroFactura()).isPresent()) {
            throw new BadRequestException(
                    "Ya existe una factura registrada con el NÃ‚Â° " + request.getNumeroFactura());
        }

        String numero = generarNumeroMIRO();
        FacturaMIRO factura = new FacturaMIRO(numero, request.getNumeroFactura(), ordenCompra, request.getFechaEmision());

        FacturaMIRO guardada = facturaMIRORepository.save(factura);

        // Generar asiento contable de la factura (cancela GR/IR + IGV + Proveedores)
        generarAsientoCompraUseCase.generarAsientoFactura(guardada);

        return FacturaMIROResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroMIRO() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "MIRO-" + Year.now().getValue() + "-" + correlativo;
    }
}
