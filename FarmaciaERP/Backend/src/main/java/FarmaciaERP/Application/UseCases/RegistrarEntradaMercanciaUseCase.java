package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.RegistrarEntradaMercanciaRequest;
import FarmaciaERP.Application.DTOs.Response.EntradaMercanciaResponse;
import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IEntradaMercanciaRepository;
import FarmaciaERP.Domain.Repositories.IOrdenCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LOG.05 Fase 04 - Entrada de mercancía y registro (MIGO). RN-F04-001 y
 * RN-F04-003: la cantidad recibida se contrasta contra la OC (tolerancia
 * 2%); RN-F04-013: lote y vencimiento obligatorios; RN-F04-014: cadena de
 * frío (2–8 °C).
 */
@Service
public class RegistrarEntradaMercanciaUseCase {

    private final IEntradaMercanciaRepository entradaMercanciaRepository;
    private final IOrdenCompraRepository ordenCompraRepository;

    public RegistrarEntradaMercanciaUseCase(IEntradaMercanciaRepository entradaMercanciaRepository,
                                             IOrdenCompraRepository ordenCompraRepository) {
        this.entradaMercanciaRepository = entradaMercanciaRepository;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @Transactional
    public EntradaMercanciaResponse ejecutar(RegistrarEntradaMercanciaRequest request) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(request.getOrdenCompraId())
                .orElseThrow(() -> new BadRequestException("Orden de Compra no encontrada: " + request.getOrdenCompraId()));

        if (entradaMercanciaRepository.existsByOrdenCompraId(ordenCompra.getId())) {
            throw new BadRequestException(
                    "Ya existe una entrada de mercancía registrada para la Orden de Compra " + ordenCompra.getNumero());
        }

        String numero = generarNumeroMIGO();
        EntradaMercancia entrada = new EntradaMercancia(
                numero,
                ordenCompra,
                request.getLote(),
                request.getFechaVencimiento(),
                request.getTemperaturaArribo(),
                request.getCantidadRecibida(),
                request.isConfirmarExcepcion()
        );

        EntradaMercancia guardada = entradaMercanciaRepository.save(entrada);
        return EntradaMercanciaResponseAssembler.toResponse(guardada);
    }

    private String generarNumeroMIGO() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "MIGO-" + Year.now().getValue() + "-" + correlativo;
    }
}
