package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.EjecutarPagoRequest;
import FarmaciaERP.Application.DTOs.Response.PagoResponse;
import FarmaciaERP.Domain.Entities.ConciliacionTresVias;
import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Entities.Pago;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConciliacionTresViasRepository;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import FarmaciaERP.Domain.Repositories.IPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.09 Fase 09 - Gestión y ejecución del pago (F110). RN: se libera para
 * pago la factura cuya conciliación de 3 vías (MRBR) haya resultado en
 * Match exitoso, se ejecuta la transferencia bancaria y se cierra
 * contablemente la cuenta por pagar.
 */
@Service
public class EjecutarPagoUseCase {

    private final IPagoRepository pagoRepository;
    private final IFacturaMIRORepository facturaMIRORepository;
    private final IConciliacionTresViasRepository conciliacionTresViasRepository;

    public EjecutarPagoUseCase(IPagoRepository pagoRepository,
                                IFacturaMIRORepository facturaMIRORepository,
                                IConciliacionTresViasRepository conciliacionTresViasRepository) {
        this.pagoRepository = pagoRepository;
        this.facturaMIRORepository = facturaMIRORepository;
        this.conciliacionTresViasRepository = conciliacionTresViasRepository;
    }

    @Transactional
    public PagoResponse ejecutar(EjecutarPagoRequest request) {
        FacturaMIRO facturaMIRO = facturaMIRORepository.findById(request.getFacturaMIROId())
                .orElseThrow(() -> new BadRequestException("Factura (MIRO) no encontrada: " + request.getFacturaMIROId()));

        if (pagoRepository.existsByFacturaMIROId(facturaMIRO.getId())) {
            throw new BadRequestException(
                    "Ya existe un pago ejecutado para la factura " + facturaMIRO.getNumeroFactura());
        }

        List<ConciliacionTresVias> conciliaciones = conciliacionTresViasRepository
                .findByOrdenCompraId(facturaMIRO.getOrdenCompra().getId());
        ConciliacionTresVias conciliacionVigente = conciliaciones.stream()
                .max(Comparator.comparing(ConciliacionTresVias::getFecha))
                .orElseThrow(() -> new BadRequestException(
                        "RN-F09-001: la Orden de Compra " + facturaMIRO.getOrdenCompra().getNumero()
                                + " no tiene una conciliación de 3 vías (MRBR) ejecutada"));

        String numero = generarNumeroPago();
        Pago pago = new Pago(numero, facturaMIRO, conciliacionVigente, request.getBanco(), request.getFechaPago());

        Pago guardado = pagoRepository.save(pago);
        return PagoResponseAssembler.toResponse(guardado);
    }

    private String generarNumeroPago() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "PAG-" + Year.now().getValue() + "-" + correlativo;
    }
}
