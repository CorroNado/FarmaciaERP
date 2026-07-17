package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearCentroCostoRequest;
import FarmaciaERP.Application.DTOs.Response.CentroCostoResponse;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.Sucursal;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.ISucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearCentroCostoUseCase {

    private final ICentroCostoRepository centroCostoRepository;
    private final ISucursalRepository sucursalRepository;

    public CrearCentroCostoUseCase(ICentroCostoRepository centroCostoRepository,
                                    ISucursalRepository sucursalRepository) {
        this.centroCostoRepository = centroCostoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Transactional
    public CentroCostoResponse ejecutar(CrearCentroCostoRequest request) {
        if (centroCostoRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe un centro de costo registrado con el cÃ³digo " + request.getCodigo());
        }

        Sucursal sucursal = null;
        if (request.getSucursalId() != null) {
            sucursal = sucursalRepository.findById(request.getSucursalId())
                    .orElseThrow(() -> new BadRequestException("Sucursal no encontrada: " + request.getSucursalId()));
        }

        CentroCosto centroCosto = new CentroCosto(request.getCodigo(), request.getNombre(), sucursal);
        return CentroCostoResponseAssembler.toResponse(centroCostoRepository.save(centroCosto));
    }
}