package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearSucursalRequest;
import FarmaciaERP.Application.DTOs.Response.SucursalResponse;
import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LOG.07 - Alta del maestro de sucursales (farmacias capilares) que
 * participan como destino en las órdenes de traslado (STO) de la Fase 06.
 */
@Service
public class CrearSucursalUseCase {

    private final ISucursalRepository sucursalRepository;

    public CrearSucursalUseCase(ISucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public SucursalResponse ejecutar(CrearSucursalRequest request) {
        if (sucursalRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("Ya existe una sucursal registrada con el código " + request.getCodigo());
        }
        Sucursal sucursal = new Sucursal(request.getCodigo(), request.getNombre());
        return SucursalResponseAssembler.toResponse(sucursalRepository.save(sucursal));
    }
}
