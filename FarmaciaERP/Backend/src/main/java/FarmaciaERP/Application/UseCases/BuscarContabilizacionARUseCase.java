package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.ContabilizacionARResponse;
import FarmaciaERP.Domain.Repositories.IContabilizacionARRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarContabilizacionARUseCase {

    private final IContabilizacionARRepository contabilizacionARRepository;

    public BuscarContabilizacionARUseCase(IContabilizacionARRepository contabilizacionARRepository) {
        this.contabilizacionARRepository = contabilizacionARRepository;
    }

    public Optional<ContabilizacionARResponse> porId(Long id) {
        return contabilizacionARRepository.findById(id).map(ContabilizacionARResponseAssembler::toResponse);
    }

    public Optional<ContabilizacionARResponse> porCierreCaja(Long cierreCajaId) {
        return contabilizacionARRepository.findByCierreCajaId(cierreCajaId).map(ContabilizacionARResponseAssembler::toResponse);
    }

    public List<ContabilizacionARResponse> todas() {
        return contabilizacionARRepository.findAll().stream()
                .map(ContabilizacionARResponseAssembler::toResponse)
                .toList();
    }
}
