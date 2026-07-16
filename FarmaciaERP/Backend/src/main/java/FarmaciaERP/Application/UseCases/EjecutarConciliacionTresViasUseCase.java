package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.EjecutarConciliacionTresViasRequest;
import FarmaciaERP.Application.DTOs.Response.ConciliacionTresViasResponse;
import FarmaciaERP.Domain.Entities.ConciliacionTresVias;
import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Entities.InspeccionCalidad;
import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConciliacionTresViasRepository;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Domain.Repositories.IFacturaMIRORepository;
import FarmaciaERP.Domain.Repositories.IInspeccionCalidadRepository;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.08 Fase 08 - Conciliación de 3 vías (3-Way Match / MRBR). RN-MM-T13: el
 * sistema compara automáticamente la Orden de Compra, la entrada de
 * mercancía (MIGO) y la factura (MIRO); cualquier discrepancia bloquea el
 * pago en MRBR hasta que se resuelva.
 */
@Service
public class EjecutarConciliacionTresViasUseCase {

    private final IConciliacionTresViasRepository conciliacionTresViasRepository;
    private final IOrdenCompraRepository ordenCompraRepository;
    private final IEntradaMercanciaRepository entradaMercanciaRepository;
    private final IFacturaMIRORepository facturaMIRORepository;
    private final IInspeccionCalidadRepository inspeccionCalidadRepository;

    public EjecutarConciliacionTresViasUseCase(IConciliacionTresViasRepository conciliacionTresViasRepository,
                                                IOrdenCompraRepository ordenCompraRepository,
                                                IEntradaMercanciaRepository entradaMercanciaRepository,
                                                IFacturaMIRORepository facturaMIRORepository,
                                                IInspeccionCalidadRepository inspeccionCalidadRepository) {
        this.conciliacionTresViasRepository = conciliacionTresViasRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.entradaMercanciaRepository = entradaMercanciaRepository;
        this.facturaMIRORepository = facturaMIRORepository;
        this.inspeccionCalidadRepository = inspeccionCalidadRepository;
    }

    @Transactional
    public ConciliacionTresViasResponse ejecutar(EjecutarConciliacionTresViasRequest request) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(request.getOrdenCompraId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada: " + request.getOrdenCompraId()));

        EntradaMercancia entradaMercancia = entradaMercanciaRepository.findByOrdenCompraId(ordenCompra.getId())
                .stream().findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "RN-F08-001: la Orden de Compra " + ordenCompra.getNumero()
                                + " no tiene entrada de mercancía (MIGO) registrada"));

        FacturaMIRO facturaMIRO = facturaMIRORepository.findByOrdenCompraId(ordenCompra.getId())
                .stream().findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "RN-F08-001: la Orden de Compra " + ordenCompra.getNumero()
                                + " no tiene factura (MIRO) registrada"));

        List<InspeccionCalidad> inspecciones = inspeccionCalidadRepository
                .findByEntradaMercanciaId(entradaMercancia.getId());
        InspeccionCalidad inspeccionCalidad = inspecciones.isEmpty() ? null : inspecciones.get(0);

        String numero = generarNumeroMRBR();
        ConciliacionTresVias conciliacion = ConciliacionTresVias.ejecutar(
                numero, ordenCompra, entradaMercancia, facturaMIRO, inspeccionCalidad);

        ConciliacionTresVias guardada = conciliacionTresViasRepository.save(conciliacion);
        return ConciliacionTresViasResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroMRBR() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "MRBR-" + Year.now().getValue() + "-" + correlativo;
    }
}
