package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.SucursalResponse;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarSucursalUseCase {

    private final ISucursalRepository sucursalRepository;

    public BuscarSucursalUseCase(ISucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public Optional<SucursalResponse> porId(Long id) {
        return sucursalRepository.findById(id).map(SucursalResponseAssembler::toResponse);
    }

    public List<SucursalResponse> todas() {
        return sucursalRepository.findAll().stream().map(SucursalResponseAssembler::toResponse).toList();
    }

    public List<SucursalResponse> activas() {
        return sucursalRepository.findAllActivas().stream().map(SucursalResponseAssembler::toResponse).toList();
    }
}
