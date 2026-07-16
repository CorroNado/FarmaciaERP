package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CotizacionResponse;
import FarmaciaERP.Domain.Enums.EstadoCotizacion;
import FarmaciaERP.Domain.Repositories.ICotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCotizacionUseCase {

    private final ICotizacionRepository cotizacionRepository;

    public BuscarCotizacionUseCase(ICotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    public Optional<CotizacionResponse> porId(Long id) {
        return cotizacionRepository.findById(id).map(CotizacionResponseAssembler::toResponse);
    }

    /**
     * SD.02.03 - Listado de cotizaciones para monitorear su estatus (VA25).
     */
    public List<CotizacionResponse> todas() {
        return cotizacionRepository.findAll().stream()
                .map(CotizacionResponseAssembler::toResponse)
                .toList();
    }

    public List<CotizacionResponse> porCliente(Long clienteId) {
        return cotizacionRepository.findByClienteId(clienteId).stream()
                .map(CotizacionResponseAssembler::toResponse)
                .toList();
    }

    public List<CotizacionResponse> porEstado(EstadoCotizacion estado) {
        return cotizacionRepository.findByEstado(estado).stream()
                .map(CotizacionResponseAssembler::toResponse)
                .toList();
    }
}
