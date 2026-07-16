package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarFacturaMIRORequest;
import FarmaciaERP.Application.DTOs.Response.FacturaMIROResponse;
import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.07 Fase 07 - Verificación de factura (MIRO). RN: MM-T12: se recibe la
 * factura electrónica del proveedor y se registra en el sistema asociándola
 * a la Orden de Compra, validando que esta cuente con entrada de mercancía
 * (MIGO) registrada y que no exista otra factura previamente asociada.
 */
@Service
public class RegistrarFacturaMIROUseCase {

    private final IFacturaMIRORepository facturaMIRORepository;
    private final IOrdenCompraRepository ordenCompraRepository;
    private final IEntradaMercanciaRepository entradaMercanciaRepository;

    public RegistrarFacturaMIROUseCase(IFacturaMIRORepository facturaMIRORepository,
                                        IOrdenCompraRepository ordenCompraRepository,
                                        IEntradaMercanciaRepository entradaMercanciaRepository) {
        this.facturaMIRORepository = facturaMIRORepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.entradaMercanciaRepository = entradaMercanciaRepository;
    }

    @Transactional
    public FacturaMIROResponse ejecutar(RegistrarFacturaMIRORequest request) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(request.getOrdenCompraId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada: " + request.getOrdenCompraId()));

        // RN-F07-004: la OC debe contar con entrada de mercancía (MIGO) registrada
        // antes de poder facturarse.
        if (!entradaMercanciaRepository.existsByOrdenCompraId(ordenCompra.getId())) {
            throw new BadRequestException(
                    "RN-F07-004: la Orden de Compra " + ordenCompra.getNumero()
                            + " no tiene una entrada de mercancía (MIGO) registrada");
        }

        if (facturaMIRORepository.existsByOrdenCompraId(ordenCompra.getId())) {
            throw new BadRequestException(
                    "Ya existe una factura (MIRO) registrada para la Orden de Compra " + ordenCompra.getNumero());
        }

        if (facturaMIRORepository.findByNumeroFactura(request.getNumeroFactura()).isPresent()) {
            throw new BadRequestException(
                    "Ya existe una factura registrada con el N° " + request.getNumeroFactura());
        }

        String numero = generarNumeroMIRO();
        FacturaMIRO factura = new FacturaMIRO(numero, request.getNumeroFactura(), ordenCompra, request.getFechaEmision());

        FacturaMIRO guardada = facturaMIRORepository.save(factura);
        return FacturaMIROResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroMIRO() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "MIRO-" + Year.now().getValue() + "-" + correlativo;
    }
}
