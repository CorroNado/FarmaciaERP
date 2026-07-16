package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarLotePagoUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;

    public BuscarLotePagoUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
    }

    public Optional<LotePagoTesoreriaResponse> porId(Long id) {
        return lotePagoTesoreriaRepository.findById(id).map(LotePagoTesoreriaResponseAssembler::toResponse);
    }

    public List<LotePagoTesoreriaResponse> todos() {
        return lotePagoTesoreriaRepository.findAll().stream()
                .map(LotePagoTesoreriaResponseAssembler::toResponse).toList();
    }
}
