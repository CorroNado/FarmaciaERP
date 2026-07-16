package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarCuentaUseCase {

    private final ICuentaRepository cuentaRepository;

    public BuscarCuentaUseCase(ICuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Optional<CuentaResponse> porId(Long id) {
        return cuentaRepository.findById(id).map(CuentaResponseAssembler::toResponse);
    }

    public List<CuentaResponse> todas() {
        return cuentaRepository.findAll().stream().map(CuentaResponseAssembler::toResponse).toList();
    }

    public List<CuentaResponse> activas() {
        return cuentaRepository.findAllActivas().stream().map(CuentaResponseAssembler::toResponse).toList();
    }
}