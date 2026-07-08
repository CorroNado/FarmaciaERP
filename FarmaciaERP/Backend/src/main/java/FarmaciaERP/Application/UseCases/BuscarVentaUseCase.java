package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.VentaResponse;
import FarmaciaERP.Domain.Enums.EstadoVenta;
import FarmaciaERP.Domain.Repositories.IVentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuscarVentaUseCase {

    private final IVentaRepository ventaRepository;

    public BuscarVentaUseCase(IVentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public Optional<VentaResponse> porId(Long id) {
        return ventaRepository.findById(id).map(VentaResponseAssembler::toResponse);
    }

    public List<VentaResponse> todas() {
        return ventaRepository.findAll().stream()
                .map(VentaResponseAssembler::toResponse)
                .toList();
    }

    public List<VentaResponse> porCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId).stream()
                .map(VentaResponseAssembler::toResponse)
                .toList();
    }

    public List<VentaResponse> porEstado(EstadoVenta estado) {
        return ventaRepository.findByEstado(estado).stream()
                .map(VentaResponseAssembler::toResponse)
                .toList();
    }

    public List<VentaResponse> porFecha(LocalDate fecha) {
        return ventaRepository.findByFecha(fecha).stream()
                .map(VentaResponseAssembler::toResponse)
                .toList();
    }
}
