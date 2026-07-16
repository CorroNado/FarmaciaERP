package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CierreCajaResponse;
import FarmaciaERP.Domain.Repositories.ICierreCajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCierreCajaUseCase {

    private final ICierreCajaRepository cierreCajaRepository;

    public BuscarCierreCajaUseCase(ICierreCajaRepository cierreCajaRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
    }

    public Optional<CierreCajaResponse> porId(Long id) {
        return cierreCajaRepository.findById(id).map(CierreCajaResponseAssembler::toResponse);
    }

    public List<CierreCajaResponse> todas() {
        return cierreCajaRepository.findAll().stream()
                .map(CierreCajaResponseAssembler::toResponse)
                .toList();
    }

    public List<CierreCajaResponse> porSucursal(Long sucursalId) {
        return cierreCajaRepository.findBySucursalId(sucursalId).stream()
                .map(CierreCajaResponseAssembler::toResponse)
                .toList();
    }
}
