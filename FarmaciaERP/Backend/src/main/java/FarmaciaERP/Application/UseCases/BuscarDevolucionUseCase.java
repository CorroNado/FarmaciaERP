package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.DevolucionResponse;
import FarmaciaERP.Domain.Repositories.IDevolucionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarDevolucionUseCase {

    private final IDevolucionRepository devolucionRepository;

    public BuscarDevolucionUseCase(IDevolucionRepository devolucionRepository) {
        this.devolucionRepository = devolucionRepository;
    }

    public Optional<DevolucionResponse> porId(Long id) {
        return devolucionRepository.findById(id).map(DevolucionResponseAssembler::toResponse);
    }

    /**
     * SD.07.02 - Historial de devoluciones registradas.
     */
    public List<DevolucionResponse> todas() {
        return devolucionRepository.findAll().stream()
                .map(DevolucionResponseAssembler::toResponse)
                .toList();
    }

    public List<DevolucionResponse> porVenta(Long ventaId) {
        return devolucionRepository.findByVentaId(ventaId).stream()
                .map(DevolucionResponseAssembler::toResponse)
                .toList();
    }
}
