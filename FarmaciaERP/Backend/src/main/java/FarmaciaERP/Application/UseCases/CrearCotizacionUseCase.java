package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearCotizacionRequest;
import FarmaciaERP.Application.DTOs.Request.DetalleCotizacionRequest;
import FarmaciaERP.Application.DTOs.Response.CotizacionResponse;
import FarmaciaERP.Domain.Entities.Cliente;
import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Entities.DetalleCotizacion;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IClienteRepository;
import FarmaciaERP.Domain.Repositories.ICotizacionRepository;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrearCotizacionUseCase {

    private final ICotizacionRepository cotizacionRepository;
    private final IClienteRepository clienteRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public CrearCotizacionUseCase(ICotizacionRepository cotizacionRepository,
                                   IClienteRepository clienteRepository,
                                   IMedicamentoRepository medicamentoRepository) {
        this.cotizacionRepository = cotizacionRepository;
        this.clienteRepository = clienteRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * SD.02.01 / SD.02.02 - Registra la solicitud de información y construye
     * la cotización con los precios vigentes del catálogo. No afecta stock.
     */
    @Transactional
    public CotizacionResponse ejecutar(CrearCotizacionRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("La cotización debe tener al menos un detalle");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new BadRequestException("Cliente no encontrado: " + request.getClienteId()));

        List<DetalleCotizacion> detalles = new ArrayList<>();
        for (DetalleCotizacionRequest detalleRequest : request.getDetalles()) {
            Medicamento medicamento = medicamentoRepository.findById(detalleRequest.getMedicamentoId())
                    .orElseThrow(() -> new BadRequestException(
                            "Medicamento no encontrado: " + detalleRequest.getMedicamentoId()));

            detalles.add(new DetalleCotizacion(medicamento, detalleRequest.getCantidad(), medicamento.getPrecio()));
        }

        Cotizacion cotizacion = new Cotizacion(cliente, detalles, request.getVigenciaDias());
        Cotizacion guardada = cotizacionRepository.save(cotizacion);

        return CotizacionResponseAssembler.toResponse(guardada);
    }
}
