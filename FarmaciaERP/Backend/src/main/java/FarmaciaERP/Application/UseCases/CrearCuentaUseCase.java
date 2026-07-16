package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearCuentaRequest;
import FarmaciaERP.Application.DTOs.Response.CuentaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearCuentaUseCase {

    private final ICuentaRepository cuentaRepository;

    public CrearCuentaUseCase(ICuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public CuentaResponse ejecutar(CrearCuentaRequest request) {
        if (cuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("Ya existe una cuenta registrada con el cÃ³digo " + request.getCodigo());
        }
        Cuenta cuenta = new Cuenta(request.getCodigo(), request.getNombre(), request.getTipoCuenta(),
                request.getNaturaleza());
        return CuentaResponseAssembler.toResponse(cuentaRepository.save(cuenta));
    }
}